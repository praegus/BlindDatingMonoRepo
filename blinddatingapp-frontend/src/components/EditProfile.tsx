import { useState, FormEvent } from 'react'
import { Configuration, Profile, Gender, GenderFromJSON, HairColor, HairColorFromJSON, MusicGenre, MusicGenreFromJSON, ProfilesApi, ErrorResponse, FieldError } from "../generated-sources";
import { useRouter } from 'next/router'
import {ErrorUtil} from '../utils'

export default function EditProfile({ data }: any) {
  const [errorResponse, setErrorResponse] = useState<ErrorResponse | null>(null);
  const router = useRouter();

  console.log('blaat', data.personalInformation.pets, data.personalInformation.pets.toString())

  if (!data.personalInformation) {
    data.personalInformation = {};
  }

  function toHairColor(value: FormDataEntryValue | null) : HairColor {
    for (const key in HairColor) {
      if (Object.prototype.hasOwnProperty.call(HairColor, key)) {
        if (key === value) {
          return HairColor[key as keyof typeof HairColor];
        }
      }
    }
    throw new Error();
  }

  function toGender(value: FormDataEntryValue | null) : Gender {
    for (const key in Gender) {
      if (Object.prototype.hasOwnProperty.call(Gender, key)) {
        if (key === value) {
          return Gender[key as keyof typeof Gender];
        }
      }
    }
    throw new Error();
  }

  function toMusicGenre(value: FormDataEntryValue | null) : MusicGenre {
    for (const key in MusicGenre) {
      if (Object.prototype.hasOwnProperty.call(MusicGenre, key)) {
        if (key === value) {
          return MusicGenre[key as keyof typeof MusicGenre];
        }
      }
    }
    throw new Error();
  }

  async function onSubmit(event: FormEvent<HTMLFormElement>) {
    setErrorResponse(null)
    event.preventDefault();

    const formData = new FormData(event.currentTarget);

    var updatedProfile: Profile = {
      username: data.username,
      firstname: formData.get("firstname")?.toString(),
      lastname: formData.get("lastname")?.toString(),
      additionalInfo: formData.get("additionalInfo")?.toString(),
      personalInformation: {
        gender: toGender(formData.get("personalInformation.gender")),
        favoriteColor: formData.get("personalInformation.favoriteColor")?.toString(),
        pets: 'true' === formData.get("personalInformation.pets"),
        hairColor: toHairColor(formData.get("personalInformation.hairColor")),
        tattoos: 'true' === formData.get("personalInformation.tattoos")?.toString(),
        sports: formData.get("personalInformation.sports")?.toString(),
        musicGenres: [toMusicGenre(formData.get("personalInformation.musicGenres"))]
      },
      dislikes: {
        gender: toGender(formData.get("dislikes.gender")),
        favoriteColor: formData.get("dislikes.favoriteColor")?.toString(),
        pets: 'true' === formData.get("dislikes.pets")?.toString(),
        hairColor: toHairColor(formData.get("dislikes.hairColor")),
        tattoos: 'true' === formData.get("dislikes.tattoos")?.toString(),
        sports: formData.get("dislikes.sports")?.toString(),
        musicGenres: [toMusicGenre(formData.get("dislikes.musicGenres"))]
      },
      preferences: {
        gender: toGender(formData.get("preferences.gender")),
        favoriteColor: formData.get("preferences.favoriteColor")?.toString(),
        pets: 'true' === formData.get("preferences.pets")?.toString(),
        hairColor: toHairColor(formData.get("preferences.hairColor")),
        tattoos: 'true' === formData.get("preferences.tattoos")?.toString(),
        sports: formData.get("preferences.sports")?.toString(),
        musicGenres: [toMusicGenre(formData.get("preferences.musicGenres"))]
      }
    };

    var profilesApi = new ProfilesApi(new Configuration({
      basePath: "http://localhost:9080"
    }));

    try {
      profilesApi.updateProfile({
        username: data.username,
        profile: updatedProfile
      }).then(() => {
        router.push(`profile?name=${data.username}&edit=false`);
        window.location.reload();
      });
    } catch (errResponse: any) {
      ErrorUtil.retrieveErrorMessage(errResponse, (json: ErrorResponse) => setErrorResponse(json))
    }
  }
  // function updateProfileName(field: string): FieldError[] {
  function getFieldErrors(field: string): FieldError[] {
    if (!errorResponse || !errorResponse.errors) return [];
    return errorResponse.errors.filter(error => error.field === field)
  }

  return (
    <div className="w-full flex">
      <div className="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4 m-auto mt-10">
        <form className="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4 m-auto mt-10" onSubmit={onSubmit}>
          <h1>Profile</h1>
          <p>username: {data.username}</p>
          <label>firstname:</label><input name="firstname" className={getFieldErrors('firstname').length ? 'border-1 border-rose-500' : ''} type="text" defaultValue={data.firstname} />
          <label>lastname:</label><input name="lastname" className={getFieldErrors('lastname').length ? 'border-1 border-rose-500' : ''} type="text" defaultValue={data.lastname} />
          <label>additionalInfo:</label><input name="additionalInfo" className={getFieldErrors('additionalInfo').length ? 'border-1 border-rose-500' : ''} type="text" defaultValue={data.additionalInfo} />

          <h3>personalInformation</h3>
          <label>gender:</label><select name="personalInformation.gender" defaultValue={data.personalInformation.gender}>{Object.keys(Gender).map(key => (<option key={key} value={key}>{key}</option>))}</select>
          <label>favorite color:</label><input name="personalInformation.favoriteColor" className={getFieldErrors('favoriteColor').length ? 'border-1 border-rose-500' : ''} type="text" defaultValue={data.personalInformation.favoriteColor} />
          <label>pets:</label><select name="personalInformation.pets" defaultValue={data.personalInformation.pets ? data.personalInformation.pets.toString() : ''}><option key={'true'} value={'true'}>yes</option><option key={'false'} value={'false'}>no</option></select>
          <label>hair color:</label><select name="personalInformation.hairColor" defaultValue={data.personalInformation.hairColor}>{Object.keys(HairColor).map(key => (<option key={key} value={key}>{key}</option>))}</select>
          <label>tattoos:</label><select name="personalInformation.tattoos" defaultValue={data.personalInformation.tattoos ? data.personalInformation.tattoos.toString() : ''}><option key={'true'} value={'true'}>yes</option><option key={'false'} value={'false'}>no</option></select>
          <label>sports:</label><input name="personalInformation.sports" className={getFieldErrors('sports').length ? 'border-1 border-rose-500' : ''} type="text" defaultValue={data.personalInformation.sports} />
          <label>music genres:</label><select name="personalInformation.musicGenres" defaultValue={data.personalInformation.musicGenres ? data.personalInformation.musicGenres[0] : ''}>{Object.keys(MusicGenre).map(key => (<option key={key} value={key}>{key}</option>))}</select>

          <h3>preferences</h3>
          <label>gender:</label><select name="preferences.gender" defaultValue={data.preferences.gender}>{Object.keys(Gender).map(key => (<option key={key} value={key}>{key}</option>))}</select>
          <label>favorite color:</label><input name="preferences.favoriteColor" className={getFieldErrors('favoriteColor').length ? 'border-1 border-rose-500' : ''} type="text" defaultValue={data.preferences.favoriteColor} />
          <label>pets:</label><select name="preferences.pets" defaultValue={data.preferences.pets ? data.preferences.pets.toString() : ''}><option key={'true'} value={'true'}>yes</option><option key={'false'} value={'false'}>no</option></select>
          <label>hair color:</label><select name="preferences.hairColor" defaultValue={data.preferences.hairColor}>{Object.keys(HairColor).map(key => (<option key={key} value={key}>{key}</option>))}</select>
          <label>tattoos:</label><select name="preferences.tattoos" defaultValue={data.preferences.tattoos ? data.preferences.tattoos.toString() : ''}><option key={'true'} value={'true'}>yes</option><option key={'false'} value={'false'}>no</option></select>
          <label>sports:</label><input name="preferences.sports" className={getFieldErrors('sports').length ? 'border-1 border-rose-500' : ''} type="text" defaultValue={data.preferences.sports} />
          <label>music genres:</label><select name="preferences.musicGenres" defaultValue={data.preferences.musicGenres ? data.preferences.musicGenres[0] : ''}>{Object.keys(MusicGenre).map(key => (<option key={key} value={key}>{key}</option>))}</select>

          <h3>dislikes</h3>
          <label>gender:</label><select name="dislikes.gender" defaultValue={data.dislikes.gender}>{Object.keys(Gender).map(key => (<option key={key} value={key}>{key}</option>))}</select>
          <label>favorite color:</label><input name="dislikes.favoriteColor" className={getFieldErrors('favoriteColor').length ? 'border-1 border-rose-500' : ''} type="text" defaultValue={data.dislikes.favoriteColor} />
          <label>pets:</label><select name="dislikes.pets" defaultValue={data.dislikes.pets ? data.dislikes.pets.toString() : ''}><option key={'true'} value={'true'}>yes</option><option key={'false'} value={'false'}>no</option></select>
          <label>hair color:</label><select name="dislikes.hairColor" defaultValue={data.dislikes.hairColor}>{Object.keys(HairColor).map(key => (<option key={key} value={key}>{key}</option>))}</select>
          <label>tattoos:</label><select name="dislikes.tattoos" defaultValue={data.dislikes.tattoos ? data.dislikes.tattoos.toString() : ''}><option key={'true'} value={'true'}>yes</option><option key={'false'} value={'false'}>no</option></select>
          <label>sports:</label><input name="dislikes.sports" className={getFieldErrors('sports').length ? 'border-1 border-rose-500' : ''} type="text" defaultValue={data.dislikes.sports} />
          <label>music genres:</label><select name="dislikes.musicGenres" defaultValue={data.dislikes.musicGenres ? data.dislikes.musicGenres[0] : ''}>{Object.keys(MusicGenre).map(key => (<option key={key} value={key}>{key}</option>))}</select>

          <button type="submit" value="Send">Send</button>
        </form>
      </div>
    </div>
  );
};
