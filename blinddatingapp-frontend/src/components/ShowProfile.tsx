export default function ShowProfile({data}: any) {
  return (
      <div className="w-full flex">
        <div className="bg-white shadow-md rounded px-8 pt-6 pb-8 mb-4 m-auto mt-10">
          <h1>Profile</h1>
          <p>username: {data.username}</p>
          <p>firstname: {data.firstname}</p>
          <p>lastname: {data.lastname}</p>
          <p>additionalInfo: {data.additionalInfo}</p>
          <h3>personalInformation</h3>
          {data.personalInformation ?
              <div>
                <p>gender: {data.personalInformation.gender}</p>
                <p>favoriteColor: {data.personalInformation.favoriteColor}</p>
                <p>pets: {data.personalInformation.pets}</p>
                <p>hairColor: {data.personalInformation.hairColor}</p>
                <p>tattoos: {data.personalInformation.tattoos}</p>
                <p>sports: {data.personalInformation.sports}</p>
                <p>musicGenres: {data.personalInformation.musicGenres}</p>
                </div>
                :
                <p>no personal information</p>
            }
          <h3>preferences</h3>
          {data.preferences ?
            <div>
              <p>gender: {data.preferences.gender}</p>
              <p>favoriteColor: {data.preferences.favoriteColor}</p>
              <p>pets: {data.preferences.pets}</p>
              <p>hairColor: {data.preferences.hairColor}</p>
              <p>tattoos: {data.preferences.tattoos}</p>
              <p>sports: {data.preferences.sports}</p>
              <p>musicGenres: {data.preferences.musicGenres}</p>
              </div>
              :
              <p>no preferences</p>
          }
          <h3>dislikes</h3>
          {data.dislikes ?
              <div>
                <p>gender: {data.dislikes.gender}</p>
                <p>favoriteColor: {data.dislikes.favoriteColor}</p>
                <p>pets: {data.dislikes.pets}</p>
                <p>hairColor: {data.dislikes.hairColor}</p>
                <p>tattoos: {data.dislikes.tattoos}</p>
                <p>sports: {data.dislikes.sports}</p>
                <p>musicGenres: {data.dislikes.musicGenres}</p>
                </div>
                :
                <p>no dislikes</p>
            }
        </div>
    </div>
  );
};
