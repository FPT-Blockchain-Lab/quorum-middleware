/* Autogenerated file. Do not edit manually. */
/* tslint:disable */
/* eslint-disable */

import type BN from "bn.js";
import type { ContractOptions } from "web3-eth-contract";
import type { EventLog } from "web3-core";
import type { EventEmitter } from "events";
import type {
  Callback,
  PayableTransactionObject,
  NonPayableTransactionObject,
  BlockType,
  ContractEventLog,
  BaseContract,
} from "./types";

export interface EventOptions {
  filter?: object;
  fromBlock?: BlockType;
  topics?: string[];
}

export interface PermissionsInterface extends BaseContract {
  constructor(
    jsonInterface: any[],
    address?: string,
    options?: ContractOptions
  ): PermissionsInterface;
  clone(): PermissionsInterface;
  methods: {
    getPermissionsImpl(): NonPayableTransactionObject<string>;

    approveAdminRole(
      _orgId: string,
      _account: string
    ): NonPayableTransactionObject<void>;

    setPolicy(
      _nwAdminOrg: string,
      _nwAdminRole: string,
      _oAdminRole: string
    ): NonPayableTransactionObject<void>;

    addSubOrg(
      _pOrgId: string,
      _orgId: string,
      _enodeId: string,
      _ip: string,
      _port: number | string | BN,
      _raftport: number | string | BN
    ): NonPayableTransactionObject<void>;

    assignAccountRole(
      _account: string,
      _orgId: string,
      _roleId: string
    ): NonPayableTransactionObject<void>;

    approveBlacklistedAccountRecovery(
      _orgId: string,
      _account: string
    ): NonPayableTransactionObject<void>;

    updateNodeStatus(
      _orgId: string,
      _enodeId: string,
      _ip: string,
      _port: number | string | BN,
      _raftport: number | string | BN,
      _action: number | string | BN
    ): NonPayableTransactionObject<void>;

    assignAdminRole(
      _orgId: string,
      _account: string,
      _roleId: string
    ): NonPayableTransactionObject<void>;

    updateNetworkBootStatus(): NonPayableTransactionObject<boolean>;

    connectionAllowed(
      _enodeId: string,
      _ip: string,
      _port: number | string | BN
    ): NonPayableTransactionObject<boolean>;

    getNetworkBootStatus(): NonPayableTransactionObject<boolean>;

    addAdminAccount(_acct: string): NonPayableTransactionObject<void>;

    setPermImplementation(
      _permImplementation: string
    ): NonPayableTransactionObject<void>;

    addOrg(
      _orgId: string,
      _enodeId: string,
      _ip: string,
      _port: number | string | BN,
      _raftport: number | string | BN,
      _account: string
    ): NonPayableTransactionObject<void>;

    addNewRole(
      _roleId: string,
      _orgId: string,
      _access: number | string | BN,
      _voter: boolean,
      _admin: boolean
    ): NonPayableTransactionObject<void>;

    approveBlacklistedNodeRecovery(
      _orgId: string,
      _enodeId: string,
      _ip: string,
      _port: number | string | BN,
      _raftport: number | string | BN
    ): NonPayableTransactionObject<void>;

    approveOrgStatus(
      _orgId: string,
      _action: number | string | BN
    ): NonPayableTransactionObject<void>;

    validateAccount(
      _account: string,
      _orgId: string
    ): NonPayableTransactionObject<boolean>;

    updateAccountStatus(
      _orgId: string,
      _account: string,
      _action: number | string | BN
    ): NonPayableTransactionObject<void>;

    addAdminNode(
      _enodeId: string,
      _ip: string,
      _port: number | string | BN,
      _raftport: number | string | BN
    ): NonPayableTransactionObject<void>;

    startBlacklistedNodeRecovery(
      _orgId: string,
      _enodeId: string,
      _ip: string,
      _port: number | string | BN,
      _raftport: number | string | BN
    ): NonPayableTransactionObject<void>;

    transactionAllowed(
      _sender: string,
      _target: string,
      _value: number | string | BN,
      _gasPrice: number | string | BN,
      _gasLimit: number | string | BN,
      _payload: string | number[]
    ): NonPayableTransactionObject<boolean>;

    isOrgAdmin(
      _account: string,
      _orgId: string
    ): NonPayableTransactionObject<boolean>;

    init(
      _breadth: number | string | BN,
      _depth: number | string | BN
    ): NonPayableTransactionObject<void>;

    removeRole(
      _roleId: string,
      _orgId: string
    ): NonPayableTransactionObject<void>;

    startBlacklistedAccountRecovery(
      _orgId: string,
      _account: string
    ): NonPayableTransactionObject<void>;

    updateOrgStatus(
      _orgId: string,
      _action: number | string | BN
    ): NonPayableTransactionObject<void>;

    isNetworkAdmin(_account: string): NonPayableTransactionObject<boolean>;

    addNode(
      _orgId: string,
      _enodeId: string,
      _ip: string,
      _port: number | string | BN,
      _raftport: number | string | BN
    ): NonPayableTransactionObject<void>;

    getPendingOp(_orgId: string): NonPayableTransactionObject<{
      0: string;
      1: string;
      2: string;
      3: string;
    }>;

    approveOrg(
      _orgId: string,
      _enodeId: string,
      _ip: string,
      _port: number | string | BN,
      _raftport: number | string | BN,
      _account: string
    ): NonPayableTransactionObject<void>;
  };
  events: {
    allEvents(options?: EventOptions, cb?: Callback<EventLog>): EventEmitter;
  };
}
