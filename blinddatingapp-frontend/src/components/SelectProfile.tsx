import {useState, FormEvent} from 'react'
import {Configuration, ProfilesApi, Profile, ErrorResponse, FieldError} from "../generated-sources";
import {useRouter} from 'next/router'
import {ErrorUtil} from '../utils'

export default function SelectProfile() {
    const [errorResponse, setErrorResponse] = useState<ErrorResponse | null>(null);
    var configuration = new Configuration({
        basePath:"http://localhost:9080"
    });

    var profilesApi = new ProfilesApi(configuration);
    const router = useRouter();

    async function onSubmit(event: FormEvent<HTMLFormElement>) {
      setErrorResponse(null)
        event.preventDefault();

        const formData = new FormData(event.currentTarget);
        const chosenName = (formData.get("name")! as string);
        console.log((event.nativeEvent as SubmitEvent).submitter)

      let action = ((event.nativeEvent as SubmitEvent).submitter as HTMLInputElement).value;
        switch (action) {
          case 'create': 
            createProfile(chosenName);
            break;
          case 'select':
            SelectExistingProfile(chosenName);
            break;
          default:
            setErrorResponse({
              detail: `${action} does not work.`
            })
        }
  }
  async function createProfile(name: string) {
    try {
      await profilesApi.createProfile({
          profile: {
              username: name
          }
      }).then(() => {
          router.push(`profile?name=${name}`)
      });
    } catch (errResponse: any) {
        console.log('test' + errResponse)
      ErrorUtil.retrieveErrorMessage(errResponse, (json: ErrorResponse) => setErrorResponse(json))
    }
  }
  async function SelectExistingProfile(name: string) {
    try {
      await profilesApi.getSingleProfile({
        username: name
      }).then((res: Profile) => {
          router.push(`profile?name=${res.username}`)
      });
    } catch (errResponse: any) {
      ErrorUtil.retrieveErrorMessage(errResponse, (json: ErrorResponse) => setErrorResponse(json))
    }
  }
  function getFieldErrors(field: string): FieldError[] {
    if (!errorResponse || !errorResponse.errors) return [];
    return errorResponse.errors.filter(error => error.field === field)
  }
  function noFieldErrorMessages() {
    console.log('test', errorResponse)
    return errorResponse && (!errorResponse.errors || !errorResponse.errors.length);
  }

  return (
    <div className="w-full flex">
      <form className="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4 m-auto mt-10" onSubmit={onSubmit}>
        <div className="mb-4">
          <label htmlFor="profileName">
            Profile
          </label>
          <input name="name" className={getFieldErrors('name').length ? 'border-1 border-rose-500':''} type="text" placeholder="ProfileName" />
          {getFieldErrors('name')
            .map((fieldError, index) => 
              <p className="mt-2 text-sm text-red-600 dark:text-red-500" key={index}>{fieldError.error}</p>
            )
          }
          
        </div>
        <p className="mt-2 text-sm text-red-600 dark:text-red-500">{noFieldErrorMessages()?errorResponse?.detail:''}</p>
        <div className="flex items-center justify-between">
          <button type="submit" value="create">Create</button>
          <button type="submit" value="select">Login</button>
        </div>
      </form>
    </div>
  );
};
