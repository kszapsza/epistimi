import { UserRole, UserSex } from '../dto/user';

export const organization = {
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
      street: 'Słonimska 1',
      postalCode: '15-950',
      city: 'Białystok',
    },
  },
  address: {
    street: 'Wrocławska 5',
    postalCode: '15-644',
    city: 'Białystok',
  },
};
