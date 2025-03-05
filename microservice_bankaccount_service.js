/*
 Navicat Premium Data Transfer

 Source Server         : MONGO_LOCAL
 Source Server Type    : MongoDB
 Source Server Version : 80004 (8.0.4)
 Source Host           : localhost:27017
 Source Schema         : microservice_bankaccount_service

 Target Server Type    : MongoDB
 Target Server Version : 80004 (8.0.4)
 File Encoding         : 65001

 Date: 05/03/2025 01:46:23
*/


// ----------------------------
// Collection structure for bank_accounts
// ----------------------------
db.getCollection("bank_accounts").drop();
db.createCollection("bank_accounts");

// ----------------------------
// Documents of bank_accounts
// ----------------------------
db.getCollection("bank_accounts").insert([ {
    _id: ObjectId("67be9698e61c0c396f37e0f0"),
    customerId: "67be562286d43e7cd15520ce",
    accountType: "AHORRO",
    _class: "com.skoy.bootcamp_microservices.model.BankAccount"
} ]);
db.getCollection("bank_accounts").insert([ {
    _id: ObjectId("67beb6597db31419cd5e7be0"),
    customerId: "67beb2df5c8259362896dff6",
    accountType: "CORRIENTE",
    _class: "com.skoy.bootcamp_microservices.model.BankAccount"
} ]);
db.getCollection("bank_accounts").insert([ {
    _id: ObjectId("67beba044c7b7549f813c255"),
    customerId: "67beb2df5c8259362896dff6",
    accountType: "CORRIENTE",
    balance: "0",
    _class: "com.skoy.bootcamp_microservices.model.BankAccount"
} ]);
db.getCollection("bank_accounts").insert([ {
    _id: ObjectId("67bebf173fd10350de01fe01"),
    customerId: "67beb2df5c8259362896dff6",
    accountType: "CORRIENTE",
    accountNumber: "4213000143603682",
    balance: "0",
    _class: "com.skoy.bootcamp_microservices.model.BankAccount"
} ]);
db.getCollection("bank_accounts").insert([ {
    _id: ObjectId("67bf5459a19db802221491b5"),
    customerId: "67beb2df5c8259362896dff6",
    accountType: "CORRIENTE",
    accountNumber: "4213000143603682",
    balance: "0",
    owners: [
        "owner-id-1",
        "owner-id-2"
    ],
    authorizedSigners: [ ],
    _class: "com.skoy.bootcamp_microservices.model.BankAccount"
} ]);
db.getCollection("bank_accounts").insert([ {
    _id: ObjectId("67bfa9b01226010dfdabfe24"),
    customerId: "67bc5049652bf702f7552f25",
    accountType: "AHORRO",
    accountNumber: "4213000143603682",
    availableBalance: "200",
    owners: [
        "owner-id-1",
        "owner-id-2"
    ],
    authorizedSigners: [ ],
    _class: "com.skoy.bootcamp_microservices.model.BankAccount"
} ]);
