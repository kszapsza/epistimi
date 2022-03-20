db.organizations.insertMany([
  {
    _id: ObjectId('6202d412889079243f77a970'),
    name: 'Szkoła Podstawowa nr 7 im. Obrońców Westerplatte',
    adminId: '621276c8e5beb130c6645547',
    status: 'DISABLED',
    directorId: '6224bd25be2e8262b5b0199a',
    address: {
      street: 'Wolska 165/24',
      postalCode: '01-258',
      city: 'Warszawa',
      countryCode: 'PL',
    },
    _class: 'pl.edu.wat.wcy.epistimi.organization.infrastructure.OrganizationMongoDbDocument'
  },
  {
    _id: ObjectId('6202db877b32d307ab8d1487'),
    name: 'Szkoła Podstawowa nr 13',
    adminId: '621276c8e5beb130c6645547',
    status: 'ENABLED',
    directorId: '621276c8e5beb130c6645547',
    address: {
      street: 'Jana Olbrachta 23/42',
      postalCode: '01-102',
      city: 'Warszawa',
      countryCode: 'PL',
    },
    _class: 'pl.edu.wat.wcy.epistimi.organization.infrastructure.OrganizationMongoDbDocument'
  },
]);