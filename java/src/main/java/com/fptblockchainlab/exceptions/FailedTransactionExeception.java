package com.fptblockchainlab.exceptions;

import lombok.experimental.StandardException;

@StandardException
/**
 * Indicate that transaction submitted on the blockchain network failed for some reasons
 */
public class FailedTransactionExeception extends Exception {
}
