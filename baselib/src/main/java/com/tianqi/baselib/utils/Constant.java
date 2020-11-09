package com.tianqi.baselib.utils;


/**
 * @创建者 Administrator
 * @创时间 2018/10/1114:10
 * @描述 常量类
 * @版本 chain
 * @更新者 rapidpay.tjchain.com.chain.constant
 * @更新时间 2018/10/11
 * @更新描述 TODO
 */
public interface Constant {

    int INTENT_REQUEST = 205;


    // 纯数字
    String DIGIT_REGEX = ".*[0-9].*";
    // 含有数字
    String CONTAIN_DIGIT_REGEX = ".*[0-9].*";
    // 纯字母
    String LETTER_REGEX = "[a-zA-Z]+";
    // 包含大写字母
    String CONTAIN_UP_LETTER_REGEX = ".*[A-Z].*";
    // 包含小写字母
    String CONTAIN_LOW_LETTER_REGEX = ".*[a-z].*";
    // 纯中文
    String CHINESE_REGEX = "[\u4e00-\u9fa5]";
    // 仅仅包含字母和数字
    String LETTER_DIGIT_REGEX = "^[a-z0-9A-Z]+$";
    String CHINESE_LETTER_REGEX = "([\u4e00-\u9fa5]+|[a-zA-Z]+)";
    String CHINESE_LETTER_DIGIT_REGEX = "^[a-z0-9A-Z\u4e00-\u9fa5]+$";


    //访问接口token的key
    String TOKEN_NAME = "Authorization";
    String LANG_CODE = "langCode";
    //登录的token,注意token保存得形式"bearer " + token
    String ACCESS_TOKEN = "chain_access_token";
    String ACCESS_PASSWORD = "tianqixuda20200821";
    public final static String Select_Key = "5287gexopi45956dtexuyhtng5986214";
    String PUBLIC_KEY_STR = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC6xXyWXD5bHdmNGLmp2T6ZDgDi30tZhLUoIyHbstRCybnmnZ420qcF7hCHHMKKbjvAyYXeAZm95USF6zx0NIB1hOPlUswl0aWH7b23WFTcyY97NsLMIfnjU2SN3i8NPBfQslXT7zsU9f6aY5BIZWNu3IUdYSR8aBkBVjz2VYy29wIDAQAB";
    String PRIVATE_KEY_STR = "MIICdAIBADALBgkqhkiG9w0BAQEEggJgMIICXAIBAAKBgQDlTNEZ9qsKr09+u1gl" +
            "2VKG5DOPF37sU+S1AmnA4fwCJRiM5jFCbXoF84Y5TyCvvMJXKgYylQW+J1EPlUVS" +
            "5x1Rd8wMLxqKRffeioF4AZ0KDRuMcO65F+XZIuan+ZYAgarTVRjmp5rXBzWEyhMJ" +
            "qUI7DAcUkbdIr1yxCDr9kqzjAwIDAQABAoGBAKE93Eh6qZow3yDKEtlCsgLDE/KK" +
            "XilHcStMEBufjfvgJBxofrksjMMElR1VpDGP9cUfew40tr4ZnansmLwMYxMjAFwJ" +
            "d3/xa6iCCGLsGD7SDD4Qm8Qc7e2+gHH7JX/r3L36pN8sdZfUj7aOY5n0GTIzw1IG" +
            "pec8I7bj3hru+VOhAkEA8rmTPCshrQ/X3DaeQSw2AvRow3edQkxAisKKvksMg1Dx" +
            "XZ/dxsKqX3KzFmHi63qcBSic2HAme2/TDnKuUdK9EQJBAPHXR1axkCHghZGbcS1T" +
            "9xhoE33FeVezyhvXpzPGNimCaC2Bs5BBJQ+TEtxnQ1QrDZ10kEyNGVCORvYJ16vi" +
            "LtMCQE+cDC2E3AO+GYeRpnArxv9LGH1b4wlWpyibzBPD27PKPY/+mifRuz2ZCiIC" +
            "MwIW8ctDWcYnaoe70pZ/iKsvmnECQAu/2cdseQQsVFDurGD3Y1ncNJY3sd2A7qDL" +
            "qWPqjVcW7tjHJGItyA8pphDvM2YzcUXlE92kPUacovXxJHlFU+ECQCVDYulXHDC1" +
            "2TD6vEKFWpc+ydwSG82thTPPrkzadcOdVDKxVSqxAsDw3BgP2KcNenHU2UW7rfbw" +
            "wMP70QooaDM=";

    String ACTION_IMAGE = "image";
    String ACTION_TEXT = "text";
    String ACTION_NUM_TEXT = "num_text";
    String INSURANCE_TITLE = "insuranceTitle";
    String GATE_WAY_TYPE_ID = "gate_way_type_id";
    String FONT_PATH = "fonts/Akrobat-ExtraBold.otf";
    String QR_CODE_ACTION = "com.chain.qr_code_action";
    String SCAN_RESULT = "scan_result";
    String COUNTRY_JSON = "[{\"countryCode\":\"CN\",\"mobCountryCode\":\"86\",\"country\":\"\\u4e2d\\u56fd \"},{\"countryCode\":\"SG\",\"mobCountryCode\":\"65\",\"country\":\"\\u65b0\\u52a0\\u5761 \"},{\"countryCode\":\"SL\",\"mobCountryCode\":\"232\",\"country\":\"\\u585e\\u62c9\\u5229\\u6602 \"},{\"countryCode\":\"AO\",\"mobCountryCode\":\"244\",\"country\":\"\\u5b89\\u54e5\\u62c9 \"},{\"countryCode\":\"AF\",\"mobCountryCode\":\"93\",\"country\":\"\\u963f\\u5bcc\\u6c57 \"},{\"countryCode\":\"AL\",\"mobCountryCode\":\"355\",\"country\":\"\\u963f\\u5c14\\u5df4\\u5c3c\\u4e9a \"},{\"countryCode\":\"DZ\",\"mobCountryCode\":\"213\",\"country\":\"\\u963f\\u5c14\\u53ca\\u5229\\u4e9a \"},{\"countryCode\":\"AD\",\"mobCountryCode\":\"376\",\"country\":\"\\u5b89\\u9053\\u5c14\\u5171\\u548c\\u56fd \"},{\"countryCode\":\"AI\",\"mobCountryCode\":\"1264\",\"country\":\"\\u5b89\\u572d\\u62c9\\u5c9b \"},{\"countryCode\":\"AG\",\"mobCountryCode\":\"1268\",\"country\":\"\\u5b89\\u63d0\\u74dc\\u548c\\u5df4\\u5e03\\u8fbe \"},{\"countryCode\":\"AR\",\"mobCountryCode\":\"54\",\"country\":\"\\u963f\\u6839\\u5ef7 \"},{\"countryCode\":\"AM\",\"mobCountryCode\":\"374\",\"country\":\"\\u4e9a\\u7f8e\\u5c3c\\u4e9a \"},{\"countryCode\":\"AU\",\"mobCountryCode\":\"61\",\"country\":\"\\u6fb3\\u5927\\u5229\\u4e9a \"},{\"countryCode\":\"AT\",\"mobCountryCode\":\"43\",\"country\":\"\\u5965\\u5730\\u5229 \"},{\"countryCode\":\"AZ\",\"mobCountryCode\":\"994\",\"country\":\"\\u963f\\u585e\\u62dc\\u7586 \"},{\"countryCode\":\"BS\",\"mobCountryCode\":\"1242\",\"country\":\"\\u5df4\\u54c8\\u9a6c \"},{\"countryCode\":\"BH\",\"mobCountryCode\":\"973\",\"country\":\"\\u5df4\\u6797 \"},{\"countryCode\":\"BD\",\"mobCountryCode\":\"880\",\"country\":\"\\u5b5f\\u52a0\\u62c9\\u56fd \"},{\"countryCode\":\"BB\",\"mobCountryCode\":\"1246\",\"country\":\"\\u5df4\\u5df4\\u591a\\u65af \"},{\"countryCode\":\"BY\",\"mobCountryCode\":\"375\",\"country\":\"\\u767d\\u4fc4\\u7f57\\u65af \"},{\"countryCode\":\"BE\",\"mobCountryCode\":\"32\",\"country\":\"\\u6bd4\\u5229\\u65f6 \"},{\"countryCode\":\"BZ\",\"mobCountryCode\":\"501\",\"country\":\"\\u4f2f\\u5229\\u5179 \"},{\"countryCode\":\"BJ\",\"mobCountryCode\":\"229\",\"country\":\"\\u8d1d\\u5b81 \"},{\"countryCode\":\"BM\",\"mobCountryCode\":\"1441\",\"country\":\"\\u767e\\u6155\\u5927\\u7fa4\\u5c9b \"},{\"countryCode\":\"BO\",\"mobCountryCode\":\"591\",\"country\":\"\\u73bb\\u5229\\u7ef4\\u4e9a \"},{\"countryCode\":\"BW\",\"mobCountryCode\":\"267\",\"country\":\"\\u535a\\u8328\\u74e6\\u7eb3 \"},{\"countryCode\":\"BR\",\"mobCountryCode\":\"55\",\"country\":\"\\u5df4\\u897f \"},{\"countryCode\":\"BN\",\"mobCountryCode\":\"673\",\"country\":\"\\u6587\\u83b1 \"},{\"countryCode\":\"BG\",\"mobCountryCode\":\"359\",\"country\":\"\\u4fdd\\u52a0\\u5229\\u4e9a \"},{\"countryCode\":\"BF\",\"mobCountryCode\":\"226\",\"country\":\"\\u5e03\\u57fa\\u7eb3\\u6cd5\\u7d22 \"},{\"countryCode\":\"MM\",\"mobCountryCode\":\"95\",\"country\":\"\\u7f05\\u7538 \"},{\"countryCode\":\"BI\",\"mobCountryCode\":\"257\",\"country\":\"\\u5e03\\u9686\\u8fea \"},{\"countryCode\":\"CM\",\"mobCountryCode\":\"237\",\"country\":\"\\u5580\\u9ea6\\u9686 \"},{\"countryCode\":\"CA\",\"mobCountryCode\":\"1\",\"country\":\"\\u52a0\\u62ff\\u5927 \"},{\"countryCode\":\"CF\",\"mobCountryCode\":\"236\",\"country\":\"\\u4e2d\\u975e\\u5171\\u548c\\u56fd \"},{\"countryCode\":\"TD\",\"mobCountryCode\":\"235\",\"country\":\"\\u4e4d\\u5f97 \"},{\"countryCode\":\"CL\",\"mobCountryCode\":\"56\",\"country\":\"\\u667a\\u5229 \"},{\"countryCode\":\"CO\",\"mobCountryCode\":\"57\",\"country\":\"\\u54e5\\u4f26\\u6bd4\\u4e9a \"},{\"countryCode\":\"CG\",\"mobCountryCode\":\"242\",\"country\":\"\\u521a\\u679c \"},{\"countryCode\":\"CK\",\"mobCountryCode\":\"682\",\"country\":\"\\u5e93\\u514b\\u7fa4\\u5c9b \"},{\"countryCode\":\"CR\",\"mobCountryCode\":\"506\",\"country\":\"\\u54e5\\u65af\\u8fbe\\u9ece\\u52a0 \"},{\"countryCode\":\"CU\",\"mobCountryCode\":\"53\",\"country\":\"\\u53e4\\u5df4 \"},{\"countryCode\":\"CY\",\"mobCountryCode\":\"357\",\"country\":\"\\u585e\\u6d66\\u8def\\u65af \"},{\"countryCode\":\"CZ\",\"mobCountryCode\":\"420\",\"country\":\"\\u6377\\u514b \"},{\"countryCode\":\"DK\",\"mobCountryCode\":\"45\",\"country\":\"\\u4e39\\u9ea6 \"},{\"countryCode\":\"DJ\",\"mobCountryCode\":\"253\",\"country\":\"\\u5409\\u5e03\\u63d0 \"},{\"countryCode\":\"DO\",\"mobCountryCode\":\"1890\",\"country\":\"\\u591a\\u7c73\\u5c3c\\u52a0\\u5171\\u548c\\u56fd \"},{\"countryCode\":\"EC\",\"mobCountryCode\":\"593\",\"country\":\"\\u5384\\u74dc\\u591a\\u5c14 \"},{\"countryCode\":\"EG\",\"mobCountryCode\":\"20\",\"country\":\"\\u57c3\\u53ca \"},{\"countryCode\":\"SV\",\"mobCountryCode\":\"503\",\"country\":\"\\u8428\\u5c14\\u74e6\\u591a \"},{\"countryCode\":\"EE\",\"mobCountryCode\":\"372\",\"country\":\"\\u7231\\u6c99\\u5c3c\\u4e9a \"},{\"countryCode\":\"ET\",\"mobCountryCode\":\"251\",\"country\":\"\\u57c3\\u585e\\u4fc4\\u6bd4\\u4e9a \"},{\"countryCode\":\"FJ\",\"mobCountryCode\":\"679\",\"country\":\"\\u6590\\u6d4e \"},{\"countryCode\":\"FI\",\"mobCountryCode\":\"358\",\"country\":\"\\u82ac\\u5170 \"},{\"countryCode\":\"FR\",\"mobCountryCode\":\"33\",\"country\":\"\\u6cd5\\u56fd \"},{\"countryCode\":\"GF\",\"mobCountryCode\":\"594\",\"country\":\"\\u6cd5\\u5c5e\\u572d\\u4e9a\\u90a3 \"},{\"countryCode\":\"GA\",\"mobCountryCode\":\"241\",\"country\":\"\\u52a0\\u84ec \"},{\"countryCode\":\"GM\",\"mobCountryCode\":\"220\",\"country\":\"\\u5188\\u6bd4\\u4e9a \"},{\"countryCode\":\"GE\",\"mobCountryCode\":\"995\",\"country\":\"\\u683c\\u9c81\\u5409\\u4e9a \"},{\"countryCode\":\"DE\",\"mobCountryCode\":\"49\",\"country\":\"\\u5fb7\\u56fd \"},{\"countryCode\":\"GH\",\"mobCountryCode\":\"233\",\"country\":\"\\u52a0\\u7eb3 \"},{\"countryCode\":\"GI\",\"mobCountryCode\":\"350\",\"country\":\"\\u76f4\\u5e03\\u7f57\\u9640 \"},{\"countryCode\":\"GR\",\"mobCountryCode\":\"30\",\"country\":\"\\u5e0c\\u814a \"},{\"countryCode\":\"GD\",\"mobCountryCode\":\"1809\",\"country\":\"\\u683c\\u6797\\u7eb3\\u8fbe \"},{\"countryCode\":\"GU\",\"mobCountryCode\":\"1671\",\"country\":\"\\u5173\\u5c9b \"},{\"countryCode\":\"GT\",\"mobCountryCode\":\"502\",\"country\":\"\\u5371\\u5730\\u9a6c\\u62c9 \"},{\"countryCode\":\"GN\",\"mobCountryCode\":\"224\",\"country\":\"\\u51e0\\u5185\\u4e9a \"},{\"countryCode\":\"GY\",\"mobCountryCode\":\"592\",\"country\":\"\\u572d\\u4e9a\\u90a3 \"},{\"countryCode\":\"HT\",\"mobCountryCode\":\"509\",\"country\":\"\\u6d77\\u5730 \"},{\"countryCode\":\"HN\",\"mobCountryCode\":\"504\",\"country\":\"\\u6d2a\\u90fd\\u62c9\\u65af \"},{\"countryCode\":\"HK\",\"mobCountryCode\":\"852\",\"country\":\"\\u9999\\u6e2f \"},{\"countryCode\":\"HU\",\"mobCountryCode\":\"36\",\"country\":\"\\u5308\\u7259\\u5229 \"},{\"countryCode\":\"IS\",\"mobCountryCode\":\"354\",\"country\":\"\\u51b0\\u5c9b \"},{\"countryCode\":\"IN\",\"mobCountryCode\":\"91\",\"country\":\"\\u5370\\u5ea6 \"},{\"countryCode\":\"ID\",\"mobCountryCode\":\"62\",\"country\":\"\\u5370\\u5ea6\\u5c3c\\u897f\\u4e9a \"},{\"countryCode\":\"IR\",\"mobCountryCode\":\"98\",\"country\":\"\\u4f0a\\u6717 \"},{\"countryCode\":\"IQ\",\"mobCountryCode\":\"964\",\"country\":\"\\u4f0a\\u62c9\\u514b \"},{\"countryCode\":\"IE\",\"mobCountryCode\":\"353\",\"country\":\"\\u7231\\u5c14\\u5170 \"},{\"countryCode\":\"IL\",\"mobCountryCode\":\"972\",\"country\":\"\\u4ee5\\u8272\\u5217 \"},{\"countryCode\":\"IT\",\"mobCountryCode\":\"39\",\"country\":\"\\u610f\\u5927\\u5229 \"},{\"countryCode\":\"JM\",\"mobCountryCode\":\"1876\",\"country\":\"\\u7259\\u4e70\\u52a0 \"},{\"countryCode\":\"JP\",\"mobCountryCode\":\"81\",\"country\":\"\\u65e5\\u672c \"},{\"countryCode\":\"JO\",\"mobCountryCode\":\"962\",\"country\":\"\\u7ea6\\u65e6 \"},{\"countryCode\":\"KH\",\"mobCountryCode\":\"855\",\"country\":\"\\u67ec\\u57d4\\u5be8 \"},{\"countryCode\":\"KZ\",\"mobCountryCode\":\"327\",\"country\":\"\\u54c8\\u8428\\u514b\\u65af\\u5766 \"},{\"countryCode\":\"KE\",\"mobCountryCode\":\"254\",\"country\":\"\\u80af\\u5c3c\\u4e9a \"},{\"countryCode\":\"KR\",\"mobCountryCode\":\"82\",\"country\":\"\\u97e9\\u56fd \"},{\"countryCode\":\"KW\",\"mobCountryCode\":\"965\",\"country\":\"\\u79d1\\u5a01\\u7279 \"},{\"countryCode\":\"KG\",\"mobCountryCode\":\"331\",\"country\":\"\\u5409\\u5c14\\u5409\\u65af\\u5766 \"},{\"countryCode\":\"LA\",\"mobCountryCode\":\"856\",\"country\":\"\\u8001\\u631d \"},{\"countryCode\":\"LV\",\"mobCountryCode\":\"371\",\"country\":\"\\u62c9\\u8131\\u7ef4\\u4e9a \"},{\"countryCode\":\"LB\",\"mobCountryCode\":\"961\",\"country\":\"\\u9ece\\u5df4\\u5ae9 \"},{\"countryCode\":\"LS\",\"mobCountryCode\":\"266\",\"country\":\"\\u83b1\\u7d22\\u6258 \"},{\"countryCode\":\"LR\",\"mobCountryCode\":\"231\",\"country\":\"\\u5229\\u6bd4\\u91cc\\u4e9a \"},{\"countryCode\":\"LY\",\"mobCountryCode\":\"218\",\"country\":\"\\u5229\\u6bd4\\u4e9a \"},{\"countryCode\":\"LI\",\"mobCountryCode\":\"423\",\"country\":\"\\u5217\\u652f\\u6566\\u58eb\\u767b \"},{\"countryCode\":\"LT\",\"mobCountryCode\":\"370\",\"country\":\"\\u7acb\\u9676\\u5b9b \"},{\"countryCode\":\"LU\",\"mobCountryCode\":\"352\",\"country\":\"\\u5362\\u68ee\\u5821 \"},{\"countryCode\":\"MO\",\"mobCountryCode\":\"853\",\"country\":\"\\u6fb3\\u95e8 \"},{\"countryCode\":\"MG\",\"mobCountryCode\":\"261\",\"country\":\"\\u9a6c\\u8fbe\\u52a0\\u65af\\u52a0 \"},{\"countryCode\":\"MW\",\"mobCountryCode\":\"265\",\"country\":\"\\u9a6c\\u62c9\\u7ef4 \"},{\"countryCode\":\"MY\",\"mobCountryCode\":\"60\",\"country\":\"\\u9a6c\\u6765\\u897f\\u4e9a \"},{\"countryCode\":\"MV\",\"mobCountryCode\":\"960\",\"country\":\"\\u9a6c\\u5c14\\u4ee3\\u592b \"},{\"countryCode\":\"ML\",\"mobCountryCode\":\"223\",\"country\":\"\\u9a6c\\u91cc \"},{\"countryCode\":\"MT\",\"mobCountryCode\":\"356\",\"country\":\"\\u9a6c\\u8033\\u4ed6 \"},{\"countryCode\":\"MU\",\"mobCountryCode\":\"230\",\"country\":\"\\u6bdb\\u91cc\\u6c42\\u65af \"},{\"countryCode\":\"MX\",\"mobCountryCode\":\"52\",\"country\":\"\\u58a8\\u897f\\u54e5 \"},{\"countryCode\":\"MD\",\"mobCountryCode\":\"373\",\"country\":\"\\u6469\\u5c14\\u591a\\u74e6 \"},{\"countryCode\":\"MC\",\"mobCountryCode\":\"377\",\"country\":\"\\u6469\\u7eb3\\u54e5 \"},{\"countryCode\":\"MN\",\"mobCountryCode\":\"976\",\"country\":\"\\u8499\\u53e4 \"},{\"countryCode\":\"MS\",\"mobCountryCode\":\"1664\",\"country\":\"\\u8499\\u7279\\u585e\\u62c9\\u7279\\u5c9b \"},{\"countryCode\":\"MA\",\"mobCountryCode\":\"212\",\"country\":\"\\u6469\\u6d1b\\u54e5 \"},{\"countryCode\":\"MZ\",\"mobCountryCode\":\"258\",\"country\":\"\\u83ab\\u6851\\u6bd4\\u514b \"},{\"countryCode\":\"NA\",\"mobCountryCode\":\"264\",\"country\":\"\\u7eb3\\u7c73\\u6bd4\\u4e9a \"},{\"countryCode\":\"NR\",\"mobCountryCode\":\"674\",\"country\":\"\\u7459\\u9c81 \"},{\"countryCode\":\"NP\",\"mobCountryCode\":\"977\",\"country\":\"\\u5c3c\\u6cca\\u5c14 \"},{\"countryCode\":\"NL\",\"mobCountryCode\":\"31\",\"country\":\"\\u8377\\u5170 \"},{\"countryCode\":\"NZ\",\"mobCountryCode\":\"64\",\"country\":\"\\u65b0\\u897f\\u5170 \"},{\"countryCode\":\"NI\",\"mobCountryCode\":\"505\",\"country\":\"\\u5c3c\\u52a0\\u62c9\\u74dc \"},{\"countryCode\":\"NE\",\"mobCountryCode\":\"227\",\"country\":\"\\u5c3c\\u65e5\\u5c14 \"},{\"countryCode\":\"NG\",\"mobCountryCode\":\"234\",\"country\":\"\\u5c3c\\u65e5\\u5229\\u4e9a \"},{\"countryCode\":\"KP\",\"mobCountryCode\":\"850\",\"country\":\"\\u671d\\u9c9c \"},{\"countryCode\":\"NO\",\"mobCountryCode\":\"47\",\"country\":\"\\u632a\\u5a01 \"},{\"countryCode\":\"OM\",\"mobCountryCode\":\"968\",\"country\":\"\\u963f\\u66fc \"},{\"countryCode\":\"PK\",\"mobCountryCode\":\"92\",\"country\":\"\\u5df4\\u57fa\\u65af\\u5766 \"},{\"countryCode\":\"PA\",\"mobCountryCode\":\"507\",\"country\":\"\\u5df4\\u62ff\\u9a6c \"},{\"countryCode\":\"PG\",\"mobCountryCode\":\"675\",\"country\":\"\\u5df4\\u5e03\\u4e9a\\u65b0\\u51e0\\u5185\\u4e9a \"},{\"countryCode\":\"PY\",\"mobCountryCode\":\"595\",\"country\":\"\\u5df4\\u62c9\\u572d \"},{\"countryCode\":\"PE\",\"mobCountryCode\":\"51\",\"country\":\"\\u79d8\\u9c81 \"},{\"countryCode\":\"PH\",\"mobCountryCode\":\"63\",\"country\":\"\\u83f2\\u5f8b\\u5bbe \"},{\"countryCode\":\"PL\",\"mobCountryCode\":\"48\",\"country\":\"\\u6ce2\\u5170 \"},{\"countryCode\":\"PF\",\"mobCountryCode\":\"689\",\"country\":\"\\u6cd5\\u5c5e\\u73bb\\u5229\\u5c3c\\u897f\\u4e9a \"},{\"countryCode\":\"PT\",\"mobCountryCode\":\"351\",\"country\":\"\\u8461\\u8404\\u7259 \"},{\"countryCode\":\"PR\",\"mobCountryCode\":\"1787\",\"country\":\"\\u6ce2\\u591a\\u9ece\\u5404 \"},{\"countryCode\":\"QA\",\"mobCountryCode\":\"974\",\"country\":\"\\u5361\\u5854\\u5c14 \"},{\"countryCode\":\"RO\",\"mobCountryCode\":\"40\",\"country\":\"\\u7f57\\u9a6c\\u5c3c\\u4e9a \"},{\"countryCode\":\"RU\",\"mobCountryCode\":\"7\",\"country\":\"\\u4fc4\\u7f57\\u65af \"},{\"countryCode\":\"LC\",\"mobCountryCode\":\"1758\",\"country\":\"\\u5723\\u5362\\u897f\\u4e9a \"},{\"countryCode\":\"VC\",\"mobCountryCode\":\"1784\",\"country\":\"\\u5723\\u6587\\u68ee\\u7279\\u5c9b \"},{\"countryCode\":\"SM\",\"mobCountryCode\":\"378\",\"country\":\"\\u5723\\u9a6c\\u529b\\u8bfa \"},{\"countryCode\":\"ST\",\"mobCountryCode\":\"239\",\"country\":\"\\u5723\\u591a\\u7f8e\\u548c\\u666e\\u6797\\u897f\\u6bd4 \"},{\"countryCode\":\"SA\",\"mobCountryCode\":\"966\",\"country\":\"\\u6c99\\u7279\\u963f\\u62c9\\u4f2f \"},{\"countryCode\":\"SN\",\"mobCountryCode\":\"221\",\"country\":\"\\u585e\\u5185\\u52a0\\u5c14 \"},{\"countryCode\":\"SC\",\"mobCountryCode\":\"248\",\"country\":\"\\u585e\\u820c\\u5c14 \"},{\"countryCode\":\"SK\",\"mobCountryCode\":\"421\",\"country\":\"\\u65af\\u6d1b\\u4f10\\u514b \"},{\"countryCode\":\"SI\",\"mobCountryCode\":\"386\",\"country\":\"\\u65af\\u6d1b\\u6587\\u5c3c\\u4e9a \"},{\"countryCode\":\"SB\",\"mobCountryCode\":\"677\",\"country\":\"\\u6240\\u7f57\\u95e8\\u7fa4\\u5c9b \"},{\"countryCode\":\"SO\",\"mobCountryCode\":\"252\",\"country\":\"\\u7d22\\u9a6c\\u91cc \"},{\"countryCode\":\"ZA\",\"mobCountryCode\":\"27\",\"country\":\"\\u5357\\u975e \"},{\"countryCode\":\"ES\",\"mobCountryCode\":\"34\",\"country\":\"\\u897f\\u73ed\\u7259 \"},{\"countryCode\":\"LK\",\"mobCountryCode\":\"94\",\"country\":\"\\u65af\\u91cc\\u5170\\u5361 \"},{\"countryCode\":\"LC\",\"mobCountryCode\":\"1758\",\"country\":\"\\u5723\\u5362\\u897f\\u4e9a \"},{\"countryCode\":\"VC\",\"mobCountryCode\":\"1784\",\"country\":\"\\u5723\\u6587\\u68ee\\u7279 \"},{\"countryCode\":\"SD\",\"mobCountryCode\":\"249\",\"country\":\"\\u82cf\\u4e39 \"},{\"countryCode\":\"SR\",\"mobCountryCode\":\"597\",\"country\":\"\\u82cf\\u91cc\\u5357 \"},{\"countryCode\":\"SZ\",\"mobCountryCode\":\"268\",\"country\":\"\\u65af\\u5a01\\u58eb\\u5170 \"},{\"countryCode\":\"SE\",\"mobCountryCode\":\"46\",\"country\":\"\\u745e\\u5178 \"},{\"countryCode\":\"CH\",\"mobCountryCode\":\"41\",\"country\":\"\\u745e\\u58eb \"},{\"countryCode\":\"SY\",\"mobCountryCode\":\"963\",\"country\":\"\\u53d9\\u5229\\u4e9a \"},{\"countryCode\":\"TW\",\"mobCountryCode\":\"886\",\"country\":\"\\u53f0\\u6e7e\\u7701 \"},{\"countryCode\":\"TJ\",\"mobCountryCode\":\"992\",\"country\":\"\\u5854\\u5409\\u514b\\u65af\\u5766 \"},{\"countryCode\":\"TZ\",\"mobCountryCode\":\"255\",\"country\":\"\\u5766\\u6851\\u5c3c\\u4e9a \"},{\"countryCode\":\"TH\",\"mobCountryCode\":\"66\",\"country\":\"\\u6cf0\\u56fd \"},{\"countryCode\":\"TG\",\"mobCountryCode\":\"228\",\"country\":\"\\u591a\\u54e5 \"},{\"countryCode\":\"TO\",\"mobCountryCode\":\"676\",\"country\":\"\\u6c64\\u52a0 \"},{\"countryCode\":\"TT\",\"mobCountryCode\":\"1809\",\"country\":\"\\u7279\\u7acb\\u5c3c\\u8fbe\\u548c\\u591a\\u5df4\\u54e5 \"},{\"countryCode\":\"TN\",\"mobCountryCode\":\"216\",\"country\":\"\\u7a81\\u5c3c\\u65af \"},{\"countryCode\":\"TR\",\"mobCountryCode\":\"90\",\"country\":\"\\u571f\\u8033\\u5176 \"},{\"countryCode\":\"TM\",\"mobCountryCode\":\"993\",\"country\":\"\\u571f\\u5e93\\u66fc\\u65af\\u5766 \"},{\"countryCode\":\"UG\",\"mobCountryCode\":\"256\",\"country\":\"\\u4e4c\\u5e72\\u8fbe \"},{\"countryCode\":\"UA\",\"mobCountryCode\":\"380\",\"country\":\"\\u4e4c\\u514b\\u5170 \"},{\"countryCode\":\"AE\",\"mobCountryCode\":\"971\",\"country\":\"\\u963f\\u62c9\\u4f2f\\u8054\\u5408\\u914b\\u957f\\u56fd \"},{\"countryCode\":\"GB\",\"mobCountryCode\":\"44\",\"country\":\"\\u82f1\\u56fd \"},{\"countryCode\":\"US\",\"mobCountryCode\":\"1\",\"country\":\"\\u7f8e\\u56fd \"},{\"countryCode\":\"UY\",\"mobCountryCode\":\"598\",\"country\":\"\\u4e4c\\u62c9\\u572d \"},{\"countryCode\":\"UZ\",\"mobCountryCode\":\"233\",\"country\":\"\\u4e4c\\u5179\\u522b\\u514b\\u65af\\u5766 \"},{\"countryCode\":\"VE\",\"mobCountryCode\":\"58\",\"country\":\"\\u59d4\\u5185\\u745e\\u62c9 \"},{\"countryCode\":\"VN\",\"mobCountryCode\":\"84\",\"country\":\"\\u8d8a\\u5357 \"},{\"countryCode\":\"YE\",\"mobCountryCode\":\"967\",\"country\":\"\\u4e5f\\u95e8 \"},{\"countryCode\":\"YU\",\"mobCountryCode\":\"381\",\"country\":\"\\u5357\\u65af\\u62c9\\u592b \"},{\"countryCode\":\"ZW\",\"mobCountryCode\":\"263\",\"country\":\"\\u6d25\\u5df4\\u5e03\\u97e6 \"},{\"countryCode\":\"ZR\",\"mobCountryCode\":\"243\",\"country\":\"\\u624e\\u4f0a\\u5c14 \"},{\"countryCode\":\"ZM\",\"mobCountryCode\":\"260\",\"country\":\"\\u8d5e\\u6bd4\\u4e9a \"}]";

    String PUT_EXTRA_COUNTRY = "nationality";
    String SELECT_CURRENCY_NAME = "select_currency_name";

    String INTENT_BANK_CARD = "intent_bank_card";
    String RECHARGE_BEAN = "recharge_bean";


    String PASSWORD_SALT_ONE = "AitdWallet8";
    String PASSWORD_SALT_TWO = "8AitdWallet";

    String ACTION_WALLET = "trans_wallet";


    int TRANSACTION_COIN_BTC = 0;
    int TRANSACTION_COIN_ETH = 1;
    int TRANSACTION_COIN_USDT = 2;


    int TRANSACTION_TYPE_SEND = 1;
    int TRANSACTION_TYPE_RECEIVE = 0;

    //交易状态，成功，失败，等待打包(0，1，2)
    int TRANSACTION_STATE_SUCCESS = 0;
    int TRANSACTION_STATE_WAITING = 2;
    int TRANSACTION_STATE_FAIL = 1;

    String TRANSACTION_COIN_NAME_BTC = "BTC";
    String IMPORT_BTC_ID = "BTC_IMPORT"; //导入的比特币id。
    String TRANSACTION_COIN_NAME_ETH = "ETH";
    String TRANSACTION_COIN_NAME_USDT = "USDT-OMNI";
    String IMPORT_USDT_ID = "USDT_IMPORT"; //导入的USDT的id。

    String WALLET_TYPE_NAME_HD = "HD-Wallet";

    //钱包类型(默认HD多链钱包0，后续有多重签名1，NFC钱包2等)
    int WALLET_TYPE_HD = 0;
    int WALLET_TYPE_MULTI = 1;
    int WALLET_TYPE_NFC = 2;
    int WALLET_TYPE_SINGLE = 3;

    String COIN_FULL_NAME_BTC = "Bitcoin";
    String COIN_FULL_NAME_ETH = "Ethereum";
    String COIN_FULL_NAME_USDT = "Tether";

    //币种的来源（0代表创建，1代表导入）
    int COIN_SOURCE_CREATE = 0;
    int COIN_SOURCE_IMPORT = 1;

    //币种类型(0,60)
    int COIN_BIP_TYPE_BTC = 0;
    int COIN_BIP_TYPE_ETH = 60;
    int COIN_BIP_TYPE_USDT = 0;

    String HTTP_ERROR = "http_error";


    String COIN_RATE_BTC = "bitcoin";
    String COIN_RATE_ETH = "ethereum";
    String COIN_RATE_USDT = "tether";


    String COIN_UNIT_BTC = "BTC";
    String COIN_UNIT_ETH = "ETH";
    String COIN_UNIT_USDT = "USDT";

    String PSD_KEY = "12345678abcdefgh";
    String PSD_IV = "0123456789876543";


    String COIN_RATE_JSON = "[\n" +
            "    {\n" +
            "        \"id\": \"bitcoin\",\n" +
            "        \"name\": \"Bitcoin\",\n" +
            "        \"symbol\": \"BTC\",\n" +
            "        \"rank\": 1,\n" +
            "        \"logo\": \"https://s1.bqiapp.com/logo/1/bitcoin.png?x-oss-process=style/coin_36_webp\",\n" +
            "        \"logo_png\": \"https://s1.bqiapp.com/logo/1/bitcoin.png?x-oss-process=style/coin_72\",\n" +
            "        \"price_usd\": 11384.0,\n" +
            "        \"price_btc\": 1.00,\n" +
            "        \"volume_24h_usd\": 8210250257.0,\n" +
            "        \"market_cap_usd\": 210732551910.0,\n" +
            "        \"available_supply\": 18511781.0,\n" +
            "        \"total_supply\": 18511781.0,\n" +
            "        \"max_supply\": 21000000.0,\n" +
            "        \"percent_change_1h\": 0.21,\n" +
            "        \"percent_change_24h\": 0.2,\n" +
            "        \"percent_change_7d\": 6.67,\n" +
            "        \"last_updated\": 1602489000,\n" +
            "        \"volume_24h_cny\": 55147429950.0,\n" +
            "        \"market_cap_cny\": 1415469477924.0,\n" +
            "        \"price_cny\": 76463.0\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": \"ethereum\",\n" +
            "        \"name\": \"Ethereum\",\n" +
            "        \"symbol\": \"ETH\",\n" +
            "        \"rank\": 2,\n" +
            "        \"logo\": \"https://s1.bqiapp.com/logo/1/ethereum.png?x-oss-process=style/coin_36_webp\",\n" +
            "        \"logo_png\": \"https://s1.bqiapp.com/logo/1/ethereum.png?x-oss-process=style/coin_72\",\n" +
            "        \"price_usd\": 374.10,\n" +
            "        \"price_btc\": 0.0329,\n" +
            "        \"volume_24h_usd\": 4026818556.0,\n" +
            "        \"market_cap_usd\": 42246670651.0,\n" +
            "        \"available_supply\": 112927512.0,\n" +
            "        \"total_supply\": 112927512.0,\n" +
            "        \"max_supply\": 112927512.0,\n" +
            "        \"percent_change_1h\": 0.35,\n" +
            "        \"percent_change_24h\": 0.47,\n" +
            "        \"percent_change_7d\": 6.56,\n" +
            "        \"last_updated\": 1602489000,\n" +
            "        \"volume_24h_cny\": 27047737562.0,\n" +
            "        \"market_cap_cny\": 283766662096.0,\n" +
            "        \"price_cny\": 2513.0\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": \"tether\",\n" +
            "        \"name\": \"Tether\",\n" +
            "        \"symbol\": \"USDT\",\n" +
            "        \"rank\": 3,\n" +
            "        \"logo\": \"https://s1.bqiapp.com/logo/1/tether.png?x-oss-process=style/coin_36_webp\",\n" +
            "        \"logo_png\": \"https://s1.bqiapp.com/logo/1/tether.png?x-oss-process=style/coin_72\",\n" +
            "        \"price_usd\": 1.00,\n" +
            "        \"price_btc\": 0.00008782,\n" +
            "        \"volume_24h_usd\": 24997140860.0,\n" +
            "        \"market_cap_usd\": 15618492849.0,\n" +
            "        \"available_supply\": 15613824940.0,\n" +
            "        \"total_supply\": 15776179170.0,\n" +
            "        \"max_supply\": 15776179170.0,\n" +
            "        \"percent_change_1h\": -0.01,\n" +
            "        \"percent_change_24h\": 0.06,\n" +
            "        \"percent_change_7d\": -0.06,\n" +
            "        \"last_updated\": 1602489000,\n" +
            "        \"volume_24h_cny\": 167903295442.0,\n" +
            "        \"market_cap_cny\": 104907854617.0,\n" +
            "        \"price_cny\": 6.72\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": \"ripple\",\n" +
            "        \"name\": \"Ripple\",\n" +
            "        \"symbol\": \"XRP\",\n" +
            "        \"rank\": 4,\n" +
            "        \"logo\": \"https://s1.bqiapp.com/logo/1/ripple.png?x-oss-process=style/coin_36_webp\",\n" +
            "        \"logo_png\": \"https://s1.bqiapp.com/logo/1/ripple.png?x-oss-process=style/coin_72\",\n" +
            "        \"price_usd\": 0.2547,\n" +
            "        \"price_btc\": 0.00002239,\n" +
            "        \"volume_24h_usd\": 1470029214.0,\n" +
            "        \"market_cap_usd\": 11128830419.0,\n" +
            "        \"available_supply\": 43685558183.0,\n" +
            "        \"total_supply\": 99991098384.0,\n" +
            "        \"max_supply\": 100000000000.0,\n" +
            "        \"percent_change_1h\": 0.11,\n" +
            "        \"percent_change_24h\": -0.49,\n" +
            "        \"percent_change_7d\": 2.73,\n" +
            "        \"last_updated\": 1602489000,\n" +
            "        \"volume_24h_cny\": 9874039227.0,\n" +
            "        \"market_cap_cny\": 74751241041.0,\n" +
            "        \"price_cny\": 1.71\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": \"binance-coin\",\n" +
            "        \"name\": \"Binance Coin\",\n" +
            "        \"symbol\": \"BNB\",\n" +
            "        \"rank\": 5,\n" +
            "        \"logo\": \"https://s1.bqiapp.com/logo/1/binance-coin.png?x-oss-process=style/coin_36_webp\",\n" +
            "        \"logo_png\": \"https://s1.bqiapp.com/logo/1/binance-coin.png?x-oss-process=style/coin_72\",\n" +
            "        \"price_usd\": 29.67,\n" +
            "        \"price_btc\": 0.002606,\n" +
            "        \"volume_24h_usd\": 596560161.0,\n" +
            "        \"market_cap_usd\": 4426281617.0,\n" +
            "        \"available_supply\": 149188549.0,\n" +
            "        \"total_supply\": 176406601.0,\n" +
            "        \"max_supply\": 200000000.0,\n" +
            "        \"percent_change_1h\": 1.74,\n" +
            "        \"percent_change_24h\": 5.56,\n" +
            "        \"percent_change_7d\": 3.27,\n" +
            "        \"last_updated\": 1602489000,\n" +
            "        \"volume_24h_cny\": 4007034949.0,\n" +
            "        \"market_cap_cny\": 29730890993.0,\n" +
            "        \"price_cny\": 199.28\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": \"bitcoin-cash\",\n" +
            "        \"name\": \"Bitcoin Cash\",\n" +
            "        \"symbol\": \"BCH\",\n" +
            "        \"rank\": 6,\n" +
            "        \"logo\": \"https://s1.bqiapp.com/logo/1/bitcoin-cash.png?x-oss-process=style/coin_36_webp\",\n" +
            "        \"logo_png\": \"https://s1.bqiapp.com/logo/1/bitcoin-cash.png?x-oss-process=style/coin_72\",\n" +
            "        \"price_usd\": 239.50,\n" +
            "        \"price_btc\": 0.0210,\n" +
            "        \"volume_24h_usd\": 2104720287.0,\n" +
            "        \"market_cap_usd\": 4376689060.0,\n" +
            "        \"available_supply\": 18274075.0,\n" +
            "        \"total_supply\": 18274075.0,\n" +
            "        \"max_supply\": 21000000.0,\n" +
            "        \"percent_change_1h\": 0.23,\n" +
            "        \"percent_change_24h\": 0.08,\n" +
            "        \"percent_change_7d\": 8.98,\n" +
            "        \"last_updated\": 1602489000,\n" +
            "        \"volume_24h_cny\": 14137195697.0,\n" +
            "        \"market_cap_cny\": 29397782747.0,\n" +
            "        \"price_cny\": 1609.0\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": \"chainlink\",\n" +
            "        \"name\": \"ChainLink\",\n" +
            "        \"symbol\": \"LINK\",\n" +
            "        \"rank\": 7,\n" +
            "        \"logo\": \"https://s1.bqiapp.com/logo/1/chainlink.png?x-oss-process=style/coin_36_webp\",\n" +
            "        \"logo_png\": \"https://s1.bqiapp.com/logo/1/chainlink.png?x-oss-process=style/coin_72\",\n" +
            "        \"price_usd\": 11.29,\n" +
            "        \"price_btc\": 0.000992,\n" +
            "        \"volume_24h_usd\": 1431360634.0,\n" +
            "        \"market_cap_usd\": 3951332752.0,\n" +
            "        \"available_supply\": 350000000.0,\n" +
            "        \"total_supply\": 1000000000.0,\n" +
            "        \"max_supply\": 1000000000.0,\n" +
            "        \"percent_change_1h\": 3.71,\n" +
            "        \"percent_change_24h\": 8.06,\n" +
            "        \"percent_change_7d\": 20.85,\n" +
            "        \"last_updated\": 1602489000,\n" +
            "        \"volume_24h_cny\": 9614306245.0,\n" +
            "        \"market_cap_cny\": 26540706962.0,\n" +
            "        \"price_cny\": 75.83\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": \"polkadot100\",\n" +
            "        \"name\": \"Polkadot NEW\",\n" +
            "        \"symbol\": \"DOT\",\n" +
            "        \"rank\": 8,\n" +
            "        \"logo\": \"https://s1.bqiapp.com/logo/1/polkadot100.png?x-oss-process=style/coin_36_webp&v=1596098161\",\n" +
            "        \"logo_png\": \"https://s1.bqiapp.com/logo/1/polkadot100.png?x-oss-process=style/coin_72&v=1596098161\",\n" +
            "        \"price_usd\": 4.36,\n" +
            "        \"price_btc\": 0.000383,\n" +
            "        \"volume_24h_usd\": 710208631.0,\n" +
            "        \"market_cap_usd\": 3931228358.0,\n" +
            "        \"available_supply\": 902350517.0,\n" +
            "        \"total_supply\": 1000000000.0,\n" +
            "        \"max_supply\": 1000000000.0,\n" +
            "        \"percent_change_1h\": 0.77,\n" +
            "        \"percent_change_24h\": 3.23,\n" +
            "        \"percent_change_7d\": 4.02,\n" +
            "        \"last_updated\": 1602489000,\n" +
            "        \"volume_24h_cny\": 4770400351.0,\n" +
            "        \"market_cap_cny\": 26405667758.0,\n" +
            "        \"price_cny\": 29.26\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": \"litecoin\",\n" +
            "        \"name\": \"Litecoin\",\n" +
            "        \"symbol\": \"LTC\",\n" +
            "        \"rank\": 9,\n" +
            "        \"logo\": \"https://s1.bqiapp.com/logo/1/litecoin.png?x-oss-process=style/coin_36_webp\",\n" +
            "        \"logo_png\": \"https://s1.bqiapp.com/logo/1/litecoin.png?x-oss-process=style/coin_72\",\n" +
            "        \"price_usd\": 50.54,\n" +
            "        \"price_btc\": 0.004440,\n" +
            "        \"volume_24h_usd\": 1214650779.0,\n" +
            "        \"market_cap_usd\": 3234159922.0,\n" +
            "        \"available_supply\": 63997010.0,\n" +
            "        \"total_supply\": 63997010.0,\n" +
            "        \"max_supply\": 84000000.0,\n" +
            "        \"percent_change_1h\": 1.11,\n" +
            "        \"percent_change_24h\": 1.94,\n" +
            "        \"percent_change_7d\": 9.13,\n" +
            "        \"last_updated\": 1602489000,\n" +
            "        \"volume_24h_cny\": 8158687817.0,\n" +
            "        \"market_cap_cny\": 21723528780.0,\n" +
            "        \"price_cny\": 339.45\n" +
            "    },\n" +
            "    {\n" +
            "        \"id\": \"bitcoin-cash-sv\",\n" +
            "        \"name\": \"Bitcoin SV\",\n" +
            "        \"symbol\": \"BSV\",\n" +
            "        \"rank\": 10,\n" +
            "        \"logo\": \"https://s1.bqiapp.com/logo/1/bitcoin-cash-sv.png?x-oss-process=style/coin_36_webp\",\n" +
            "        \"logo_png\": \"https://s1.bqiapp.com/logo/1/bitcoin-cash-sv.png?x-oss-process=style/coin_72\",\n" +
            "        \"price_usd\": 171.60,\n" +
            "        \"price_btc\": 0.0151,\n" +
            "        \"volume_24h_usd\": 863768447.0,\n" +
            "        \"market_cap_usd\": 3135327747.0,\n" +
            "        \"available_supply\": 18271627.0,\n" +
            "        \"total_supply\": 21000000.0,\n" +
            "        \"max_supply\": 21000000.0,\n" +
            "        \"percent_change_1h\": 0.28,\n" +
            "        \"percent_change_24h\": -0.08,\n" +
            "        \"percent_change_7d\": 6.97,\n" +
            "        \"last_updated\": 1602489000,\n" +
            "        \"volume_24h_cny\": 5801846282.0,\n" +
            "        \"market_cap_cny\": 21059682944.0,\n" +
            "        \"price_cny\": 1153.0\n" +
            "    }\n" +
            "]";

}
