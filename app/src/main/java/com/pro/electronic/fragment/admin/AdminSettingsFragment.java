package com.pro.electronic.fragment.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.pro.electronic.R;
import com.pro.electronic.activity.MailLoginActivity;
import com.pro.electronic.activity.admin.AdminFeedbackActivity;
import com.pro.electronic.activity.admin.AdminRevenueActivity;
import com.pro.electronic.activity.admin.AdminRoleActivity;
import com.pro.electronic.activity.admin.AdminTopProductActivity;
import com.pro.electronic.activity.admin.AdminVoucherActivity;
import com.pro.electronic.prefs.DataStoreManager;
import com.pro.electronic.utils.Constant;
import com.pro.electronic.utils.GlobalFunction;

public class AdminSettingsFragment extends Fragment {

    private View mView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_admin_settings, container, false);

        setupScreen();

        return mView;
    }

    private void setupScreen() {
        TextView tvEmail = mView.findViewById(R.id.tv_email);
        tvEmail.setText(DataStoreManager.getUser().getEmail());
        TextView tvManageRole = mView.findViewById(R.id.tv_manage_role);
        if (Constant.MAIN_ADMIN.equals(DataStoreManager.getUser().getEmail())) {
            tvManageRole.setVisibility(View.VISIBLE);
        } else {
            tvManageRole.setVisibility(View.GONE);
        }

        tvManageRole.setOnClickListener(view -> onClickManageRole());
        mView.findViewById(R.id.tv_manage_revenue).setOnClickListener(view -> onClickManageRevenue());
        mView.findViewById(R.id.tv_manage_top_product).setOnClickListener(view -> onClickManageTopProduct());
        mView.findViewById(R.id.tv_manage_voucher).setOnClickListener(view -> onClickManageVoucher());
        mView.findViewById(R.id.tv_manage_feedback).setOnClickListener(view -> onClickManageFeedback());
        mView.findViewById(R.id.tv_sign_out).setOnClickListener(view -> onClickSignOut());
    }

    private void onClickManageRole() {
        GlobalFunction.startActivity(getActivity(), AdminRoleActivity.class);
    }

    private void onClickManageRevenue() {
        GlobalFunction.startActivity(getActivity(), AdminRevenueActivity.class);
    }

    private void onClickManageTopProduct() {
        GlobalFunction.startActivity(getActivity(), AdminTopProductActivity.class);
    }

    private void onClickManageVoucher() {
        GlobalFunction.startActivity(getActivity(), AdminVoucherActivity.class);
    }

    private void onClickManageFeedback() {
        GlobalFunction.startActivity(getActivity(), AdminFeedbackActivity.class);
    }

    private void onClickSignOut() {
        if (getActivity() == null) return;
        FirebaseAuth.getInstance().signOut();
        DataStoreManager.setUser(null);
        GlobalFunction.startActivity(getActivity(), MailLoginActivity.class);
        getActivity().finishAffinity();
    }
}
