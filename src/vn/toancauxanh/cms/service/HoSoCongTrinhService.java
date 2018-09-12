package vn.toancauxanh.cms.service;

import org.apache.commons.collections.MapUtils;
import org.zkoss.util.resource.Labels;

import com.querydsl.jpa.impl.JPAQuery;

import vn.toancauxanh.gg.model.HoSoCongTrinh;
import vn.toancauxanh.gg.model.QHoSoCongTrinh;
import vn.toancauxanh.service.BasicService;

public class HoSoCongTrinhService extends BasicService<HoSoCongTrinh>{
		
	
	public JPAQuery<HoSoCongTrinh> getTargetQuery() {
		String tuKhoa = MapUtils.getString(argDeco(), Labels.getLabel("param.tukhoa"),"").trim();
		String trangThai = MapUtils.getString(argDeco(), Labels.getLabel("param.trangthai"), "");

		JPAQuery<HoSoCongTrinh> phongBan = find(HoSoCongTrinh.class)
				.where(QHoSoCongTrinh.hoSoCongTrinh.trangThai.ne(core().TT_DA_XOA));

		if (tuKhoa != null && !tuKhoa.isEmpty()) {
			String tukhoa = "%" + tuKhoa + "%";
			phongBan.where(QHoSoCongTrinh.hoSoCongTrinh.ten.like(tukhoa));	
		}
		if (!trangThai.isEmpty()) {
			phongBan.where(QHoSoCongTrinh.hoSoCongTrinh.trangThai.eq(trangThai));
		}
		phongBan.orderBy(QHoSoCongTrinh.hoSoCongTrinh.ngaySua.desc());
		return phongBan;
	}
}
