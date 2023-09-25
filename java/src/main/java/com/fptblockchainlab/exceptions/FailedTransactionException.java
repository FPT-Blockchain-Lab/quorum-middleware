package com.fptblockchainlab.exceptions;

import lombok.experimental.StandardException;

@StandardException
/**
 * Indicate that transaction submitted on the blockchain network failed for some reason
 */
public class FailedTransactionException extends Exception {
}
