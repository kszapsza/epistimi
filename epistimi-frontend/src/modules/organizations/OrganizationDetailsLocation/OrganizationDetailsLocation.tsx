import './OrganizationDetailsLocation.scss';
import 'leaflet/dist/leaflet.css';
import { Address } from '../../../dto/address';
import { MapContainer, Marker, TileLayer, useMap } from 'react-leaflet';
import { NominatimResponse } from '../../../dto/external/nominatim';
import { OrganizationDetailsKeyValue } from '../OrganizationDetailsKeyValue';
import { Spinner } from '../../../components';
import { useEffect, useState } from 'react';
import axios from 'axios';
import L from 'leaflet';

const MAP_ZOOM = 16;

interface CenterMapProps {
  lat: number;
  lon: number;
}

const CenterMap = (
  { lat, lon }: CenterMapProps,
): JSX.Element => {
  const map = useMap();

  useEffect(() => {
    lat && lon && map.setView([lat, lon], MAP_ZOOM);
  }, [lat, lon]);

  return <></>;
};

type OrganizationDetailsLocationProps = Address;

export const OrganizationDetailsLocation = (
  props: OrganizationDetailsLocationProps,
): JSX.Element => {
  const [lat, setLat] = useState<number>();
  const [lon, setLon] = useState<number>();
  const [loading, setLoading] = useState<boolean>(true);

  useEffect(() => {
    setupLeafletIcons();
    setLoading(true);

    axios.get<NominatimResponse[]>(buildNominatimUrl())
      .then((response) => {
        setLoading(false);
        setLat(Number(response.data[0].lat));
        setLon(Number(response.data[0].lon));
      })
      .catch(() => {
        setLoading(false);
      });
  }, [props.street, props.postalCode, props.street, props.countryCode]);

  const setupLeafletIcons = (): void => {
    // eslint-disable-next-line  @typescript-eslint/no-explicit-any
    delete (L.Icon.Default.prototype as any)._getIconUrl;

    L.Icon.Default.mergeOptions({
      iconRetinaUrl: require('leaflet/dist/images/marker-icon-2x.png'),
      iconUrl: require('leaflet/dist/images/marker-icon.png'),
      shadowUrl: require('leaflet/dist/images/marker-shadow.png'),
    });
  };

  const buildNominatimUrl = (): string => {
    return 'https://nominatim.openstreetmap.org/search?format=json&q='
      + encodeURIComponent(`${props.street}, ${props.postalCode} ${props.city}, ${formatCountry()}`);
  };

  const formatCountry = (): string => {
    return new Intl.DisplayNames('pl-PL', { type: 'region' })
      .of(props.countryCode) ?? props.countryCode;
  };

  return (
    <div className={'organization-location'}>
      {loading && <Spinner/>}
      {!loading && <>
        {lat && lon && <div className={'organization-map'}>
          <MapContainer className={'organization-map-container'}>
            <TileLayer
              attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
              url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
            />
            <Marker position={[lat, lon]}/>
            <CenterMap lat={lat} lon={lon}/>
          </MapContainer>
        </div>}
        <div className={'organization-address'}>
          <h4>Adres i kontakt</h4>
          <OrganizationDetailsKeyValue
            label={'Ulica'}
            value={props.street}/>
          <OrganizationDetailsKeyValue
            label={'Kod pocztowy'}
            value={props.postalCode}/>
          <OrganizationDetailsKeyValue
            label={'Miasto'}
            value={props.city}/>
          <OrganizationDetailsKeyValue
            label={'Kraj'}
            value={formatCountry()}/>
        </div>
      </>}
    </div>
  );
};
