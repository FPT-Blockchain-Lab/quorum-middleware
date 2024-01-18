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

export type Approved = ContractEventLog<{
  caller: string;
  stage: string;
  subStage: string;
  documentID: string;
  approvedTime: string;
  organization: string;
  0: string;
  1: string;
  2: string;
  3: string;
  4: string;
  5: string;
}>;

export interface LC extends BaseContract {
  constructor(
    jsonInterface: any[],
    address?: string,
    options?: ContractOptions
  ): LC;
  clone(): LC;
  methods: {
    _hashToStage(arg0: string | number[]): NonPayableTransactionObject<{
      stage: string;
      subStage: string;
      0: string;
      1: string;
    }>;

    _owner(): NonPayableTransactionObject<string>;

    amend(): NonPayableTransactionObject<void>;

    amended(): NonPayableTransactionObject<boolean>;

    approve(
      _caller: string,
      _stage: number | string | BN,
      _subStage: number | string | BN,
      _content: [
        string | number[],
        number | string | BN,
        string | number[],
        number | string | BN,
        (string | number[])[],
        string,
        string | number[],
        string | number[]
      ]
    ): NonPayableTransactionObject<void>;

    checkProposer(
      _proposer: string,
      _stage: number | string | BN
    ): NonPayableTransactionObject<string>;

    close(): NonPayableTransactionObject<void>;

    factory(): NonPayableTransactionObject<string>;

    getContent(
      _stage: number | string | BN,
      _subStage: number | string | BN
    ): NonPayableTransactionObject<
      [string, string, string, string, string[], string, string, string]
    >;

    getCounter(): NonPayableTransactionObject<string>;

    getInvolvedParties(): NonPayableTransactionObject<string[]>;

    getMigrateInfo(
      _hashes: (string | number[])[]
    ): NonPayableTransactionObject<{
      _stages: [string, string][];
      _packages: [
        string,
        [string, string, string, string, string[], string, string, string]
      ][];
      0: [string, string][];
      1: [
        string,
        [string, string, string, string, string[], string, string, string]
      ][];
    }>;

    getRootHash(): NonPayableTransactionObject<string>;

    getRootList(): NonPayableTransactionObject<string[]>;

    getStatus(): NonPayableTransactionObject<string[]>;

    hashToStage(
      _hash: string | number[]
    ): NonPayableTransactionObject<[string, string]>;

    isClosed(): NonPayableTransactionObject<boolean>;

    lcType(): NonPayableTransactionObject<string>;

    numOfSubStage(
      _stage: number | string | BN
    ): NonPayableTransactionObject<string>;

    setCounter(
      _newValue: number | string | BN
    ): NonPayableTransactionObject<void>;
  };
  events: {
    Approved(cb?: Callback<Approved>): EventEmitter;
    Approved(options?: EventOptions, cb?: Callback<Approved>): EventEmitter;

    allEvents(options?: EventOptions, cb?: Callback<EventLog>): EventEmitter;
  };

  once(event: "Approved", cb: Callback<Approved>): void;
  once(event: "Approved", options: EventOptions, cb: Callback<Approved>): void;
}
