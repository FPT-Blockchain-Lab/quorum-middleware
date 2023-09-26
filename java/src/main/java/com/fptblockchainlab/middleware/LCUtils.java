package com.fptblockchainlab.middleware;

import com.fptblockchainlab.bindings.lc.LC;
import com.fptblockchainlab.bindings.lc.StandardLC;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.Sign;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class LCUtils {
    public static final String EMPTY_BYTES = "0x";
    public static String DEFAULT_ROOT_HASH = "0xc5d2460186f7233c927e7db2dcc703c0e500b653ca82273b7bfad8045d85a470";

    public enum Stage {
        // BLC_03
        PHAT_HANH_LC, // phát hành LC
        // BLC_04
        XUAT_TRINH_TCD_BCT, // xuất trình thư chỉ dẫn bộ chứng từ
        // BLC_05
        THONG_BAO_BCT_MH, // thông báo bộ chứng từ mua hàng
        // BLC_06
        CHAP_NHAN_THANH_TOAN, // chấp nhận thanh toán
        //BLC_07_08
        LC_NHPH_NHXT, // lc thường: ngân hàng phát hành - ngân hàng xuất trình
        LC_NHXT_BTH, // lc thường: ngân hàng xuất trình - bên thụ hưởng
        UPAS_NHTT_NHXT, // lc upas: ngân hàng tài trợ - ngân hàng xuất trình
        UPAS_NHXT_BTH, // lc upas: ngân hàng xuất trình - bên thụ hưởng
        UPAS_NHPH_NHTT, // lc upas: ngân hàng phát hành - ngân hàng tài trợ
    }

    public static class StageContent {
        public LC.Stage stage;

        public String rootHash;

        public StageContent(LC.Stage stage, String rootHash) {
            this.stage = stage;
            this.rootHash = rootHash;
        }
    }

    public static List<LC.Stage> getLcStatus(StandardLC lc) throws Exception {
        BigInteger rootSubStage = lc.numOfSubStage(BigInteger.ONE).send();
        List<BigInteger> lcStatus = lc.getStatus().send();
        List<LC.Stage> rootStages = new ArrayList<>();

        for (int i = 0; i < rootSubStage.intValue(); i++) {
            rootStages.add(new LC.Stage(BigInteger.ONE, BigInteger.valueOf(i)));
        }

        List<LC.Stage> lcStages = calculateStages(lcStatus);
        List<LC.Stage> allStatus = new ArrayList<>();
        allStatus.addAll(rootStages);
        allStatus.addAll(lcStages);

        return allStatus;
    }

    private static List<LC.Stage> calculateStages(List<BigInteger> lastestStages) {
        List<LC.Stage> res = new ArrayList<>();

        for (int i = 0; i < lastestStages.size(); i++) {
            for (int j = 0; j < lastestStages.get(i).intValue(); j++) {
                res.add(new LC.Stage(BigInteger.valueOf(j + 1), BigInteger.valueOf(i + 1)));
            }
        }

        return res;
    }
}
