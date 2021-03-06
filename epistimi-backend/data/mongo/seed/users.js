db.users.insertMany([
  {
    _id: ObjectId('621275f336dbc952c914e21f'),
    firstName: 'Adam',
    lastName: 'Nowak',
    role: 'EPISTIMI_ADMIN',
    username: 'epistimi_admin',
    passwordHash: '$2a$12$Pw7FhRTRUgxV6nmfSOiJte27EhZ4qPjo87LwGlm78AAKEzDpdy/tG',
    pesel: '63030991511',
    sex: 'MALE',
    email: 'a.nowak@gmail.com',
    phoneNumber: '+48789984721',
    address: {
      street: 'Szkolna 17',
      postalCode: '15-640',
      city: 'Białystok',
      countryCode: 'PL',
    },
    _class: 'pl.edu.wat.wcy.epistimi.user.infrastructure.UserMongoDbDocument',
  },
  {
    _id: ObjectId('621276c8e5beb130c6645547'),
    firstName: 'Jerzy',
    lastName: 'Pomidorek',
    role: 'ORGANIZATION_ADMIN',
    username: 'organization_admin',
    passwordHash: '$2a$12$7GRFzY3yW1T3MHLZLULR3uwMrxc7t3zISMjwCB2TjW03hPhn5HeTO',
    pesel: '57082522652',
    sex: 'MALE',
    email: 'j.pomidorek@gmail.com',
    phoneNumber: '+48794214368',
    address: {
      street: 'Okopowa 59',
      postalCode: '01-043',
      city: 'Warszawa',
      countryCode: 'PL',
    },
    _class: 'pl.edu.wat.wcy.epistimi.user.infrastructure.UserMongoDbDocument',
  },
  {
    _id: ObjectId('6224bd25be2e8262b5b0199a'),
    firstName: 'Marzena',
    lastName: 'Marchewka',
    role: 'TEACHER',
    username: 'teacher',
    passwordHash: '$2a$12$HwiBu9m9EIZGG1FEct10IOVlDnzDHcjJDqS83Vyc5/5LvQawLQ.Wi',
    pesel: '72113071222',
    sex: 'FEMALE',
    email: 'm.marchewka@outlook.com',
    phoneNumber: '+48516783945',
    address: {
      street: 'Leona Kruczkowskiego 6',
      postalCode: '00-412',
      city: 'Warszawa',
      countryCode: 'PL',
    },
    _class: 'pl.edu.wat.wcy.epistimi.user.infrastructure.UserMongoDbDocument',
  },
  {
    _id: ObjectId('62126c3e8c89484e7e29b8de'),
    firstName: 'Jan',
    lastName: 'Kowalski',
    role: 'STUDENT',
    username: 'student',
    passwordHash: '$2a$12$G/2YnElb2WKZmXZ4uCSuW.wP6vA1YYhiGoCoHx6Y1A0EVtRl6r/A6',
    pesel: '12313021472',
    sex: 'MALE',
    email: null,
    phoneNumber: null,
    address: {
      street: 'Grzybowska 6/10',
      postalCode: '00-131',
      city: 'Warszawa',
      countryCode: 'PL',
    },
    _class: 'pl.edu.wat.wcy.epistimi.user.infrastructure.UserMongoDbDocument',
  },
  {
    _id: ObjectId('6224bca1a88aed211290a210'),
    firstName: 'Henryk',
    lastName: 'Kowalski',
    role: 'PARENT',
    username: 'parent',
    passwordHash: '$2a$12$t6GdMjUsuurY8uEI4Vim2.jTAKW596j46juDmoe4XxQwvdB4BLSvW',
    pesel: '76040467675',
    sex: 'MALE',
    email: 'h.kowalski@interia.pl',
    phoneNumber: '+48792414440',
    address: {
      street: 'Grzybowska 6/10',
      postalCode: '00-131',
      city: 'Warszawa',
      countryCode: 'PL',
    },
    _class: 'pl.edu.wat.wcy.epistimi.user.infrastructure.UserMongoDbDocument',
  },
]);