package app.girin.trn.evm.lib

import io.ethers.core.types.Address

object PEG_PRECOMPILE {
    val address: Address = Address("0x0000000000000000000000000000000000000793")
}

object SFT_PRECOMPILE {
    val address: Address = Address("0x00000000000000000000000000000000000006c3")
    private val abi: List<String> = listOf(
        "event InitializeSftCollection(address indexed collectionOwner, address indexed precompileAddress)",
        "function initializeCollection(address owner, bytes name, bytes metadataPath, address[] royaltyAddresses, uint32[] royaltyEntitlements) returns (address, uint32)"
    )

    enum class Index {
        EVENT_INITIALIZE_SFT_COLLECTION,
        FUNCTION_INITIALIZE_COLLECTION
    }

    fun getAbi(index: Index): String = abi[index.ordinal]
}

object FEE_PROXY_PRECOMPILE {
    val address: Address = Address("0x00000000000000000000000000000000000004bb")
    private val abi: List<String> = listOf(
        "function callWithFeePreferences(address asset, uint128 maxPayment, address target, bytes input)"
    )

    enum class Index {
        FUNCTION_CALL_WITH_FEE_PREFERENCES
    }

    fun getAbi(index: Index): String = abi[index.ordinal]
}

object ERC20_PRECOMPILE {
    private val abi: List<String> = listOf(
        "event Transfer(address indexed from, address indexed to, uint256 value)",
        "event Approval(address indexed owner, address indexed spender, uint256 value)",
        "function approve(address spender, uint256 amount) public returns (bool)",
        "function allowance(address owner, address spender) public view returns (uint256)",
        "function balanceOf(address who) public view returns (uint256)",
        "function name() public view returns (string memory)",
        "function symbol() public view returns (string memory)",
        "function decimals() public view returns (uint8)",
        "function transfer(address who, uint256 amount)",
    )

    enum class Index {
        EVENT_TRANSFER,
        EVENT_APPROVAL,
        FUNCTION_APPROVE,
        FUNCTION_ALLOWANCE,
        FUNCTION_BALANCE_OF,
        FUNCTION_NAME,
        FUNCTION_SYMBOL,
        FUNCTION_DECIMALS,
        FUNCTION_TRANSFER,
    }

    fun getAbi(index: Index): String = abi[index.ordinal]
}

object NFT_PRECOMPILE {
    val address: Address = Address("0x00000000000000000000000000000000000006b9")

    private val abi: List<String> = listOf(
        "event InitializeCollection(address indexed collectionOwner, address precompileAddress)",
        "function initializeCollection(address owner, bytes name, uint32 maxIssuance, bytes metadataPath, address[] royaltyAddresses, uint32[] royaltyEntitlements) returns (address, uint32)"
    )

    enum class Index {
        EVENT_INITIALIZE_COLLECTION,
        FUNCTION_INITIALIZE_COLLECTION
    }

    fun getAbi(index: Index): String = abi[index.ordinal]
}

object OWNABLE_PRECOMPILE {
    private val abi: List<String> = listOf(
        "event OwnershipTransferred(address indexed previousOwner, address indexed newOwner)",
        "function owner() returns (address)",
        "function renounceOwnership()",
        "function transferOwnership(address owner)"
    )

    enum class Index {
        EVENT_OWNERSHIP_TRANSFERRED,
        FUNCTION_OWNER,
        FUNCTION_RENOUNCE_OWNERSHIP,
        FUNCTION_TRANSFER_OWNERSHIP
    }

    fun getAbi(index: Index): String = abi[index.ordinal]
}

object ERC721_PRECOMPILE {
    private val abi: List<String> = listOf(
        // ERC721
        "event Transfer(address indexed from, address indexed to, uint256 indexed tokenId)",
        "event Approval(address indexed owner, address indexed approved, uint256 indexed tokenId)",
        "event ApprovalForAll(address indexed owner, address indexed operator, bool approved)",

        "function balanceOf(address who) returns (uint256)",
        "function ownerOf(uint256 tokenId) returns (address)",
        "function safeTransferFrom(address from, address to, uint256 tokenId)",
        "function transferFrom(address from, address to, uint256 tokenId)",
        "function approve(address to, uint256 tokenId)",
        "function getApproved(uint256 tokenId) returns (address)",
        "function setApprovalForAll(address operator, bool _approved)",
        "function isApprovedForAll(address owner, address operator) returns (bool)",

        // ERC721 Metadata
        "function name() returns (string memory)",
        "function symbol() returns (string memory)",
        "function tokenURI(uint256 tokenId) returns (string memory)",

        // Root specific precompiles
        "event MaxSupplyUpdated(uint32 maxSupply)",
        "event BaseURIUpdated(string baseURI)",

        "function totalSupply() external returns (uint256)",
        "function mint(address owner, uint32 quantity)",
        "function setMaxSupply(uint32 maxSupply)",
        "function setBaseURI(bytes baseURI)",
        "function ownedTokens(address who, uint16 limit, uint32 cursor) returns (uint32, uint32, uint32[] memory)",
    )

    enum class Index {
        EVENT_TRANSFER,
        EVENT_APPROVAL,
        EVENT_APPROVAL_FOR_ALL,
        FUNCTION_BALANCE_OF,
        FUNCTION_OWNER_OF,
        FUNCTION_SAFE_TRANSFER_FROM,
        FUNCTION_TRANSFER_FROM,
        FUNCTION_APPROVE,
        FUNCTION_GET_APPROVED,
        FUNCTION_SET_APPROVAL_FOR_ALL,
        FUNCTION_IS_APPROVED_FOR_ALL,
        FUNCTION_NAME,
        FUNCTION_SYMBOL,
        FUNCTION_TOKEN_URI,
        EVENT_MAX_SUPPLY_UPDATED,
        EVENT_BASE_URI_UPDATED,
        FUNCTION_TOTAL_SUPPLY,
        FUNCTION_MINT,
        FUNCTION_SET_MAX_SUPPLY,
        FUNCTION_SET_BASE_URI,
        FUNCTION_OWNED_TOKENS
    }

    fun getAbi(index: Index): String = abi[index.ordinal]
}

object FUTUREPASS_PRECOMPILE {
    private val abi: List<String> = listOf(
        "event FuturepassDelegateRegistered(address indexed futurepass, address indexed delegate, uint8 proxyType)",
        "event FuturepassDelegateUnregistered(address indexed futurepass, address delegate)",
        "event Executed(uint8 indexed callType, address indexed target, uint256 indexed value, bytes4 data)",
        "event ContractCreated(uint8 indexed callType, address indexed contractAddress, uint256 indexed value, bytes32 salt)",
        "function delegateType(address delegate) external returns (uint8)",
        "function registerDelegateWithSignature(address delegate, uint8 proxyType, uint32 deadline, bytes memory signature) external",
        "function unregisterDelegate(address delegate) external",
        "function proxyCall(uint8 callType, address callTo, uint256 value, bytes memory callData) external payable"
    )

    enum class Index {
        EVENT_FUTUREPASS_DELEGATE_REGISTERED,
        EVENT_FUTUREPASS_DELEGATE_UNREGISTERED,
        EVENT_EXECUTED,
        EVENT_CONTRACT_CREATED,
        FUNCTION_DELEGATE_TYPE,
        FUNCTION_REGISTER_DELEGATE_WITH_SIGNATURE,
        FUNCTION_UNREGISTER_DELEGATE,
        FUNCTION_PROXY_CALL
    }

    fun getAbi(index: Index): String = abi[index.ordinal]
}

object FUTUREPASS_REGISTRAR_PRECOMPILE {
    val address: Address = Address("0x000000000000000000000000000000000000FFFF")

    private val abi: List<String> = listOf(
        "event FuturepassCreated(address indexed futurepass, address owner)",
        "function futurepassOf(address owner) external returns (address)",
        "function create(address owner) external returns (address)"
    )

    enum class Index {
        EVENT_FUTUREPASS_CREATED,
        FUNCTION_FUTUREPASS_OF,
        FUNCTION_CREATE
    }

    fun getAbi(index: Index): String = abi[index.ordinal]
}

object DEX_PRECOMPILE {
    val address: Address = Address("0x000000000000000000000000000000000000DDDD")

    private val abi: List<String> = listOf(
        "function addLiquidity(address tokenA, address tokenB, uint256 amountADesired, uint256 amountBDesired, uint256 amountAMin, uint256 amountBMin, address to, uint256 deadline) returns (uint256 amountA, uint256 amountB, uint256 liquidity)",
        "function addLiquidityETH(address token, uint256 amountTokenDesired, uint256 amountTokenMin, uint256 amountETHMin, address to, uint256 deadline) payable returns (uint256 amountToken, uint256 amountETH, uint256 liquidity)",
        "function getAmountIn(uint256 amountOut, uint256 reserveIn, uint256 reserveOut) pure returns (uint256 amountIn)",
        "function getAmountOut(uint256 amountIn, uint256 reserveIn, uint256 reserveOut) pure returns (uint256 amountOut)",
        "function getAmountsIn(uint256 amountOut, address[] path) returns (uint256[] amounts)",
        "function getAmountsOut(uint256 amountIn, address[] path) returns (uint256[] amounts)",
        "function quote(uint256 amountA, uint256 reserveA, uint256 reserveB) pure returns (uint256 amountB)",
        "function removeLiquidity(address tokenA, address tokenB, uint256 liquidity, uint256 amountAMin, uint256 amountBMin, address to, uint256 deadline) returns (uint256 amountA, uint256 amountB)",
        "function removeLiquidityETH(address token, uint256 liquidity, uint256 amountTokenMin, uint256 amountETHMin, address to, uint256 deadline) returns (uint256 amountToken, uint256 amountETH)",
        "function swapETHForExactTokens(uint256 amountOut, address[] path, address to, uint256 deadline) payable returns (uint256[] amounts)",
        "function swapExactETHForTokens(uint256 amountOutMin, address[] path, address to, uint256 deadline) payable returns (uint256[] amounts)",
        "function swapExactTokensForETH(uint256 amountIn, uint256 amountOutMin, address[] path, address to, uint256 deadline) returns (uint256[] amounts)",
        "function swapExactTokensForTokens(uint256 amountIn, uint256 amountOutMin, address[] path, address to, uint256 deadline) returns (uint256[] amounts)",
        "function swapTokensForExactETH(uint256 amountOut, uint256 amountInMax, address[] path, address to, uint256 deadline) returns (uint256[] amounts)",
        "function swapTokensForExactTokens(uint256 amountOut, uint256 amountInMax, address[] path, address to, uint256 deadline) returns (uint256[] amounts)",
    )

    enum class Index {
        FUNCTION_ADD_LIQUIDITY,
        FUNCTION_ADD_LIQUIDITY_ETH,
        FUNCTION_GET_AMOUNT_IN,
        FUNCTION_GET_AMOUNT_OUT,
        FUNCTION_GET_AMOUNTS_IN,
        FUNCTION_GET_AMOUNTS_OUT,
        FUNCTION_QUOTE,
        FUNCTION_REMOVE_LIQUIDITY,
        FUNCTION_REMOVE_LIQUIDITY_ETH,
        FUNCTION_SWAP_ETH_FOR_EXACT_TOKENS,
        FUNCTION_SWAP_EXACT_ETH_FOR_TOKENS,
        FUNCTION_SWAP_EXACT_TOKENS_FOR_ETH,
        FUNCTION_SWAP_EXACT_TOKENS_FOR_TOKENS,
        FUNCTION_SWAP_TOKENS_FOR_EXACT_ETH,
        FUNCTION_SWAP_TOKENS_FOR_EXACT_TOKENS
    }

    fun getAbi(index: Index): String = abi[index.ordinal]
}