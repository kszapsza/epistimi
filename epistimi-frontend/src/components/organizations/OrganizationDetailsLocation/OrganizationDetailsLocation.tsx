import './OrganizationDetailsLocation.scss';
import 'leaflet/dist/leaflet.css';
import { Address } from '../../../dto/address';
import { Location } from '../../../dto/location';
import { MapContainer, Marker, TileLayer, useMap } from 'react-leaflet';
import { OrganizationDetailsKeyValue } from '../OrganizationDetailsKeyValue';
import { Title } from '@mantine/core';
import { useEffect } from 'react';
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

interface OrganizationDetailsLocationProps {
  address: Address;
  location?: Location;
}

export const OrganizationDetailsLocation = (
  { address, location }: OrganizationDetailsLocationProps,
): JSX.Element => {
  useEffect(() => {
    // eslint-disable-next-line  @typescript-eslint/no-explicit-any
    delete (L.Icon.Default.prototype as any)._getIconUrl;

    L.Icon.Default.mergeOptions({
      iconRetinaUrl: require('leaflet/dist/images/marker-icon-2x.png'),
      iconUrl: require('leaflet/dist/images/marker-icon.png'),
      shadowUrl: require('leaflet/dist/images/marker-shadow.png'),
    });
  }, []);

  return (
    <div className={'organization-location'}>
      {location && <div className={'organization-map'}>
        <MapContainer className={'organization-map-container'}>
          <TileLayer
            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          />
          <Marker position={[location.latitude, location.longitude]}/>
          <CenterMap lat={location.latitude} lon={location.longitude}/>
        </MapContainer>
      </div>}
      <div className={'organization-address'}>
        <Title order={4}>Adres i kontakt</Title>
        <OrganizationDetailsKeyValue
          label={'Ulica'}
          value={address.street}/>
        <OrganizationDetailsKeyValue
          label={'Kod pocztowy'}
          value={address.postalCode}/>
        <OrganizationDetailsKeyValue
          label={'Miasto'}
          value={address.city}/>
      </div>
    </div>
  );
};
