package com.pro.electronic.listener;

import com.pro.electronic.model.Voucher;

public interface IOnAdminManagerVoucherListener {
    void onClickUpdateVoucher(Voucher voucher);
    void onClickDeleteVoucher(Voucher voucher);
}
