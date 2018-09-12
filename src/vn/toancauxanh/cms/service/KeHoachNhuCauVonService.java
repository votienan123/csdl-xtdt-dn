package vn.toancauxanh.cms.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.MapUtils;
import org.camunda.bpm.engine.impl.RepositoryServiceImpl;
import org.camunda.bpm.engine.impl.pvm.PvmTransition;
import org.camunda.bpm.engine.task.IdentityLink;
import org.camunda.bpm.engine.task.Task;
import org.zkoss.bind.BindUtils;
import org.zkoss.util.resource.Labels;

import com.querydsl.jpa.impl.JPAQuery;

import vn.toancauxanh.gg.model.KeHoachNhuCauVon;
import vn.toancauxanh.gg.model.QKeHoachNhuCauVon;
import vn.toancauxanh.model.VaiTro;
import vn.toancauxanh.service.BasicService;

public class KeHoachNhuCauVonService extends BasicService<KeHoachNhuCauVon> implements Serializable{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 8950327520538434417L;

	public JPAQuery<KeHoachNhuCauVon> getTargetQuery() {
		String tuKhoa = MapUtils.getString(argDeco(), Labels.getLabel("param.tukhoa"),"").trim();
		String trangThai = MapUtils.getString(argDeco(), Labels.getLabel("param.trangthai"), "");
		
//		List<String> listNhom = core().getNhanVien().getListNhom();
//		List<Task> list = core().getProcess()
//				.getTaskService()
//				.createTaskQuery()
//				//.processVariableValueEquals("user", core().getNhanVien().getId())
//				//.caseInstanceVariableValueEquals("user", core().getNhanVien().getId())
//				.taskCandidateGroupIn(listNhom)
//				.list();
//		for (Task task : list) {
//			System.out.println("task: "  +  task.getId() + " ___ " + task.getName() + " __ ");
//			List<IdentityLink> identitys = core().getProcess()
//			.getTaskService().getIdentityLinksForTask(task.getId());
//			for (IdentityLink i: identitys) {
//				System.out.println("i: " + i.getGroupId() + " ___ " + i.toString());
//			}
//		}
		
		JPAQuery<KeHoachNhuCauVon> phongBan = find(KeHoachNhuCauVon.class)
				.where(QKeHoachNhuCauVon.keHoachNhuCauVon.trangThai.ne(core().TT_DA_XOA));

		if (tuKhoa != null && !tuKhoa.isEmpty()) {
			String tukhoa = "%" + tuKhoa + "%";
			phongBan.where(QKeHoachNhuCauVon.keHoachNhuCauVon.ten.like(tukhoa));	
		}
		if (!trangThai.isEmpty()) {
			phongBan.where(QKeHoachNhuCauVon.keHoachNhuCauVon.trangThai.eq(trangThai));
		}
		phongBan.orderBy(QKeHoachNhuCauVon.keHoachNhuCauVon.ngaySua.desc());
		
		setCount(phongBan.fetchCount());
		BindUtils.postNotifyChange(null, null, this, "count");
		return phongBan;
	}
}
