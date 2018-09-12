package vn.toancauxanh.cms.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.MapUtils;
import org.zkoss.bind.annotation.Init;
import org.zkoss.util.resource.Labels;

import com.querydsl.jpa.impl.JPAQuery;

import vn.toancauxanh.gg.model.GioiTinh;
import vn.toancauxanh.gg.model.QGioiTinh;
import vn.toancauxanh.service.BasicService;

public class GioiTinhService extends BasicService<GioiTinh>{
		
	@Init
	public void init() {
		List<GioiTinh> list = find(GioiTinh.class)
				.where(QGioiTinh.gioiTinh.trangThai.ne(core().TT_DA_XOA))
				.fetch();
		if (list == null || list.isEmpty()) {
			bootstrapGioiTinh();
		}
	}
	
	public List<GioiTinh> getListGioiTinh() {
		List<GioiTinh> list = new ArrayList<GioiTinh>();
		list = find(GioiTinh.class)
				.where(QGioiTinh.gioiTinh.trangThai.eq(core().TT_AP_DUNG))
				.orderBy(QGioiTinh.gioiTinh.ten.asc())
				.fetch();
		return list;
	}
	
	public List<GioiTinh> getListGioiTinhAndNull() {
		List<GioiTinh> list = new ArrayList<GioiTinh>();
		list.add(null);
		list.addAll(getListGioiTinh());
		return list;
	}
	
	public JPAQuery<GioiTinh> getTargetQuery() {
		String tuKhoa = MapUtils.getString(argDeco(), Labels.getLabel("param.tukhoa"),"").trim();
		String trangThai = MapUtils.getString(argDeco(), Labels.getLabel("param.trangthai"), "");

		JPAQuery<GioiTinh> phongBan = find(GioiTinh.class)
				.where(QGioiTinh.gioiTinh.trangThai.ne(core().TT_DA_XOA));

		if (tuKhoa != null && !tuKhoa.isEmpty()) {
			String tukhoa = "%" + tuKhoa + "%";
			phongBan.where(QGioiTinh.gioiTinh.ten.like(tukhoa));	
		}
		if (!trangThai.isEmpty()) {
			phongBan.where(QGioiTinh.gioiTinh.trangThai.eq(trangThai));
		}
		phongBan.orderBy(QGioiTinh.gioiTinh.ngaySua.desc());
		return phongBan;
	}
	
	public void bootstrapGioiTinh() {
		GioiTinh nam = new GioiTinh();
		nam.setTen("Nam");
		nam.saveNotShowNotification();
		
		GioiTinh nu = new GioiTinh();
		nu.setTen("Nữ");
		nu.saveNotShowNotification();
	}
}
