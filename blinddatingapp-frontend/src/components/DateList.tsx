import { RomanticDate } from "../generated-sources";
import moment from 'moment';
export default function DateList({data}: any) {

  return (
    <div className="w-full flex">
        {data?.map((date: RomanticDate) => (
          <div key={date.address?.street}>
            <h1>Dates</h1>
            <h3>When</h3>
            <p>{moment(new Date((date.time as any) * 1000)).format("DD MMM yyyy HH:MM")}</p>
            <h3>Where</h3>
            <p>Street: {date.address?.street} {date.address?.streetNumber}</p>
            <p>PostalCode: {date.address?.postalCode}</p>
            <p>City: {date.address?.city}</p>
            <h3>What to bring</h3>
            <p>{date.itemToBring}</p>
            
          </div>
        ))}
    </div>
  );
};