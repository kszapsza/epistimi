import { OrganizationResponse, OrganizationStatus } from '../dto/organization';
import { UserRole, UserSex } from '../dto/user';

const organization = {
  id: '42',
  name: 'SP7',
  admin: {
    id: '42',
    firstName: 'Jan',
    lastName: 'Kowalski',
    role: UserRole.ORGANIZATION_ADMIN,
    username: 'j.kowalski',
    pesel: '10210155874',
    sex: UserSex.MALE,
    email: 'j.kowalski@gmail.com',
    phoneNumber: '+48123456789',
    address: {
      street: 'Szkolna 17',
      postalCode: '15-640',
      city: 'Białystok',
    },
  },
  address: {
    street: 'Wrocławska 5',
    postalCode: '15-644',
    city: 'Białystok',
  },
};

export const enabledOrganization: OrganizationResponse = {
  ...organization,
  id: '43',
  status: OrganizationStatus.ENABLED,
};

export const disabledOrganization: OrganizationResponse = {
  ...organization,
  id: '44',
  status: OrganizationStatus.DISABLED,
};
