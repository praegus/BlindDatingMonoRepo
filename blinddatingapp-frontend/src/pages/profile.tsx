import ShowProfile from '../components/ShowProfile';
import EditProfile from '../components/EditProfile';
import { useState, useEffect } from 'react'
import NavBar from '../components/NavBar';
import MatchNotifier from '../components/MatchNotifier';
import DateList from '../components/DateList';
import { ErrorResponse, Configuration, ProfilesApi, Profile } from '@/generated-sources';
import { ErrorUtil } from '@/utils';
import { useRouter } from 'next/router';

export default function Profile() {
  const [errorResponse, setErrorResponse] = useState<ErrorResponse | null>(null);
  const [data, setData] = useState<Profile>();
  var profilesApi = new ProfilesApi(new Configuration({
    basePath: "http://localhost:9080"
  }));
  const router = useRouter();
  const editMode = router.query.edit == 'true' ? true : false

  useEffect(() => {
    const fetchData = async () => {
      var profileName =
        Array.isArray(router.query.name) ? router.query.name[0] :
          router.query.name!;
      try {
        var profiles = await profilesApi.getSingleProfile({
          username: profileName
        });
        setData(profiles);
      } catch (errResponse: any) {
        ErrorUtil.retrieveErrorMessage(errResponse, (json: ErrorResponse) => setErrorResponse(json))
      }
    }
    if (router.isReady) fetchData();
  }, [router.isReady]);

  function goEdit(e) {
    router.push({ path: '/profile', query: { name: router.query.name, edit: true } })
  }
  function goView(e) {
    router.push({ path: '/profile', query: { name: router.query.name, edit: false } })
  }

  return (
    <div>
      <NavBar data={data?.username} />
      <MatchNotifier data={data?.username} />
      <DateList data={data?.dates} />
      {data ?
        <div>
          <div className="w-full flex">
            {!editMode ? <ShowProfile data={data} /> : <EditProfile data={data} />}
            <br />
            {!editMode ? <button type="submit" value="Edit" onClick={goEdit}>Edit</button> : <button type="submit" value="Back" onClick={goView}>View</button>}
          </div>
        </div>
        : 'no data'}
    </div>
  );
};

