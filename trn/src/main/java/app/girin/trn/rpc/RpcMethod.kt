package app.girin.trn.rpc

enum class RpcMethod(val methodName: String) {
    AccountNextIndex("account_nextIndex"),

    ChainGetBlock("chain_getBlock"),
    ChainGetFinalizedHead("chain_getFinalizedHead"),
    ChainGetBlockHash("chain_getBlockHash"),

    AuthorSubmitExtrinsic("author_submitExtrinsic"),

    StateGetRuntimeVersion("state_getRuntimeVersion"),
    StateCall("state_call"),
    StateGetStorage("state_getStorage"),

    DexGetAmountsIn("dex_getAmountsIn")
}