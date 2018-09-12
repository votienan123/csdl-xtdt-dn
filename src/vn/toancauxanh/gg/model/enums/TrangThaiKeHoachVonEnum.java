package vn.toancauxanh.gg.model.enums;

public enum TrangThaiKeHoachVonEnum {

	LUU_TAM("Lưu tạm"),
	CHO_DUYET("Chờ duyệt"),
	DA_KIEM_TRA("Đã kiểm tra"),
	TU_CHOI_PHE_DUYET("Từ chối phê duyệt"),
	DA_DUYET("Đã duyệt");

	String text;

	TrangThaiKeHoachVonEnum(final String txt) {
		text = txt;
	}

	public String getText() {
		return text;
	}
}
