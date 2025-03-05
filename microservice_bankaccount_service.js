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

 Date: 05/03/2025 08:08:23
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
    _id: ObjectId("67c8183a5c3a86772242850c"),
    customerId: "67c80f32d082587cc57705a6",
    accountType: "AHORRO",
    accountNumber: "4213000143603682",
    availableBalance: "0",
    owners: [
        "owner-id-1",
        "owner-id-2"
    ],
    authorizedSigners: [ ],
    maintenanceCommission: "0",
    _class: "com.skoy.bootcamp_microservices.model.BankAccount"
} ]);
db.getCollection("bank_accounts").insert([ {
    _id: ObjectId("67c822a6c5323d08f13bab0d"),
    customerId: "67bc5049652bf702f7552f25",
    accountType: "AHORRO",
    accountNumber: "4213000143603682",
    availableBalance: "12120.00",
    owners: [
        "owner-id-1",
        "owner-id-2"
    ],
    authorizedSigners: [ ],
    maintenanceCommission: "0",
    maxFreeTransactions: NumberInt("3"),
    transactionCommission: "5",
    transactionCount: NumberInt("17"),
    _class: "com.skoy.bootcamp_microservices.model.BankAccount"
} ]);
db.getCollection("bank_accounts").insert([ {
    _id: ObjectId("67c824cdc5323d08f13bab0e"),
    customerId: "67bc5049652bf702f7552f25",
    accountType: "CORRIENTE",
    accountNumber: "1234567890000000",
    availableBalance: "2000",
    owners: [
        "owner-id-1",
        "owner-id-2"
    ],
    authorizedSigners: [ ],
    maintenanceCommission: "0",
    maxFreeTransactions: NumberInt("3"),
    transactionCommission: "5",
    transactionCount: NumberInt("1"),
    _class: "com.skoy.bootcamp_microservices.model.BankAccount"
} ]);
db.getCollection("bank_accounts").insert([ {
    _id: ObjectId("67c82a650a6f735c96144fa1"),
    customerId: "67c82a25d082587cc57705a8",
    accountType: "CORRIENTE",
    accountNumber: "1234567890000001",
    availableBalance: "0",
    owners: [
        "owner-id-1",
        "owner-id-2"
    ],
    authorizedSigners: [ ],
    maintenanceCommission: "0",
    maxFreeTransactions: NumberInt("3"),
    transactionCommission: "5",
    transactionCount: NumberInt("1"),
    _class: "com.skoy.bootcamp_microservices.model.BankAccount"
} ]);
