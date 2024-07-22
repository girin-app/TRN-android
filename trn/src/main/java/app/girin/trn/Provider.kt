package app.girin.trn

enum class RpcMethod(val methodName: String) {
    AccountNextIndex("account_nextIndex"),

    ChainGetBlock("chain_getBlock"),
    ChainGetFinalizedHead("chain_getFinalizedHead"),
    ChainGetBlockHash("chain_getBlockHash"),

    AuthorSubmitExtrinsic("author_submitExtrinsic"),

    StateGetRuntimeVersion("state_getRuntimeVersion"),

    DexGetAmountsIn("dex_getAmountsIn")
}