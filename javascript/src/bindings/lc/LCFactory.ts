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

export type NewLC = ContractEventLog<{
  lcType: string;
  documentID: string;
  creator: string;
  lcContractAddr: string;
  0: string;
  1: string;
  2: string;
  3: string;
}>;

export interface LCFactory extends BaseContract {
  constructor(
    jsonInterface: any[],
    address?: string,
    options?: ContractOptions
  ): LCFactory;
  clone(): LCFactory;
  methods: {
    WRAPPER(): NonPayableTransactionObject<string>;

    amend(
      _executor: string,
      _documentId: number | string | BN,
      _parties: string[],
      _lcType: number | string | BN
    ): NonPayableTransactionObject<string>;

    create(
      _parties: string[],
      _content: [
        string | number[],
        number | string | BN,
        string | number[],
        number | string | BN,
        (string | number[])[],
        string,
        string | number[],
        string | number[]
      ],
      _lcType: number | string | BN
    ): NonPayableTransactionObject<string>;

    getLCAddress(
      _documentId: number | string | BN
    ): NonPayableTransactionObject<string[]>;

    management(): NonPayableTransactionObject<string>;

    setLCManagement(_management: string): NonPayableTransactionObject<void>;
  };
  events: {
    NewLC(cb?: Callback<NewLC>): EventEmitter;
    NewLC(options?: EventOptions, cb?: Callback<NewLC>): EventEmitter;

    allEvents(options?: EventOptions, cb?: Callback<EventLog>): EventEmitter;
  };

  once(event: "NewLC", cb: Callback<NewLC>): void;
  once(event: "NewLC", options: EventOptions, cb: Callback<NewLC>): void;
}