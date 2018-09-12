
package vn.toancauxanh.service;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.collections.MapUtils;
import org.camunda.bpm.engine.ProcessEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zkoss.util.resource.Labels;
import org.zkoss.zhtml.Object;

import vn.toancauxanh.cms.service.GioiTinhService;
import vn.toancauxanh.cms.service.HoSoCongTrinhService;
import vn.toancauxanh.cms.service.KeHoachNhuCauVonService;
import vn.toancauxanh.model.VaiTro;

@Configuration
@Controller
public class Entry extends BaseObject<Object> {
	static Entry instance;

	@Value("${trangthai.apdung}")
	public String TT_AP_DUNG = "";
	@Value("${trangthai.daxoa}")
	public String TT_DA_XOA = "";
	@Value("${trangthai.khongapdung}")
	public String TT_KHONG_AP_DUNG = "";	

	@Value("${action.xem}")
	public String XEM = ""; // duoc xem bat ky
	@Value("${action.list}")
	public String LIST = ""; // duoc vao trang list search url
	@Value("${action.sua}")
	public String SUA = ""; // duoc sua bat ky
	@Value("${action.xoa}")
	public String XOA = ""; // duoc xoa bat ky
	@Value("${action.them}")
	public String THEM = ""; // duoc them
	
	@Value("${url.nguoidung}")
	public String NGUOIDUNG = "";
	@Value("${url.vaitro}")
	public String VAITRO = "";	
	@Value("${url.kehoachnhucauvon}")
	public String KEHOACHNHUCAUVON = "";	
	@Value("${url.baocaokehoachvon}")
	public String BAOCAOKEHOACHVON = "";
	@Value("${url.hosocongtrinh}")
	public String HOSOCONGTRINH = "";
	
	// uend
	public char CHAR_CACH = ':';
	public String CACH = CHAR_CACH + "";
	
	@Value("${url.vaitro}" + ":" + "${action.xem}")
	public String VAITROXEM;
	@Value("${url.vaitro}" + ":" + "${action.them}")
	public String VAITROTHEM = "";
	@Value("${url.vaitro}" + ":" + "${action.list}")
	public String VAITROLIST = "";
	@Value("${url.vaitro}" + ":" + "${action.xoa}")
	public String VAITROXOA = "";
	@Value("${url.vaitro}" + ":" + "${action.sua}")
	public String VAITROSUA = "";
	
	@Value("${url.nguoidung}" + ":" + "${action.xem}")
	public String NGUOIDUNGXEM = "";
	@Value("${url.nguoidung}" + ":" + "${action.them}")
	public String NGUOIDUNGTHEM = "";
	@Value("${url.nguoidung}" + ":" + "${action.list}")
	public String NGUOIDUNGLIST = "";
	@Value("${url.nguoidung}" + ":" + "${action.xoa}")
	public String NGUOIDUNGXOA = "";
	@Value("${url.nguoidung}" + ":" + "${action.sua}")
	public String NGUOIDUNGSUA = "";	
	
	@Value("${url.kehoachnhucauvon}" + ":" + "${action.xem}")
	public String KEHOACHNHUCAUVONXEM = "";
	@Value("${url.kehoachnhucauvon}" + ":" + "${action.them}")
	public String KEHOACHNHUCAUVONTHEM = "";
	@Value("${url.kehoachnhucauvon}" + ":" + "${action.list}")
	public String KEHOACHNHUCAUVONLIST = "";
	@Value("${url.kehoachnhucauvon}" + ":" + "${action.xoa}")
	public String KEHOACHNHUCAUVONXOA = "";
	@Value("${url.kehoachnhucauvon}" + ":" + "${action.sua}")
	public String KEHOACHNHUCAUVONSUA = "";
	
	@Value("${url.baocaokehoachvon}" + ":" + "${action.xem}")
	public String BAOCAOKEHOACHVONXEM = "";
	@Value("${url.baocaokehoachvon}" + ":" + "${action.them}")
	public String BAOCAOKEHOACHVONTHEM = "";
	@Value("${url.baocaokehoachvon}" + ":" + "${action.list}")
	public String BAOCAOKEHOACHVONLIST = "";
	@Value("${url.baocaokehoachvon}" + ":" + "${action.xoa}")
	public String BAOCAOKEHOACHVONXOA = "";
	@Value("${url.baocaokehoachvon}" + ":" + "${action.sua}")
	public String BAOCAOKEHOACHVONSUA = "";
	
	@Value("${url.hosocongtrinh}" + ":" + "${action.xem}")
	public String HOSOCONGTRINHXEM = "";
	@Value("${url.hosocongtrinh}" + ":" + "${action.them}")
	public String HOSOCONGTRINHTHEM = "";
	@Value("${url.hosocongtrinh}" + ":" + "${action.list}")
	public String HOSOCONGTRINHLIST = "";
	@Value("${url.hosocongtrinh}" + ":" + "${action.xoa}")
	public String HOSOCONGTRINHXOA = "";
	@Value("${url.hosocongtrinh}" + ":" + "${action.sua}")
	public String HOSOCONGTRINHSUA = "";
	
	// aend
	public String[] getRESOURCES() {
		return new String[] {
				NGUOIDUNG, VAITRO, HOSOCONGTRINH, BAOCAOKEHOACHVON, KEHOACHNHUCAUVON};
	}

	public String[] getACTIONS() {
		return new String[] { LIST, XEM, THEM, SUA, XOA};
	}

	static {
		File file = new File(Labels.getLabel("filestore.root") + File.separator + Labels.getLabel("filestore.folder"));
		if (!file.exists()) {
			if (file.mkdir()) {
				System.out.println("Directory mis is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		}
	}
	@Autowired
	public Environment env;

	@Autowired
	DataSource dataSource;
	
	@Autowired
	@Lazy
	ProcessEngine processEngine;

	public Entry() {
		super();
		setCore();
		instance = this;
	}

	@Bean
	public FilterRegistrationBean cacheFilter() {
		FilterRegistrationBean rs = new FilterRegistrationBean(new CacheFilter());
		rs.addUrlPatterns("*.css");
		rs.addUrlPatterns("*.js");
		rs.addUrlPatterns("*.wpd");
		rs.addUrlPatterns("*.wcs");
		rs.addUrlPatterns("*.jpg");
		rs.addUrlPatterns("*.jpeg");
		rs.addUrlPatterns("*.png");
		return rs;
	}

//	@Bean
//	public FilterRegistrationBean loginFilter() {
//		FilterRegistrationBean rs = new FilterRegistrationBean(new LoginFilter());
//		rs.addUrlPatterns("/*");
//		return rs;
//	}
	
	Map<String, String> pathMap = new HashMap<>();

	{
		MapUtils.putAll(pathMap,
				new String[] { 
						"doanhnghiep", "doanhnghiep", 
						"gioithieu", "gioithieu", 
						"cosophaply", "cosophaply"
				});
	}

	@RequestMapping(value = "/")
	public String cp() {
		return "forward:/WEB-INF/zul/home1.zul?resource=hosocongtrinh&action=lietke&file=/WEB-INF/zul/hosocongtrinh/list.zul";
	}

	@RequestMapping(value = "/{path:.+$}")
	public String cp(@PathVariable String path) {
		if (path.equals("hosocongtrinh")) {
			return cp();
		}
		return "forward:/WEB-INF/zul/home1.zul?resource=" + path + "&action=lietke&file=/WEB-INF/zul/" + path
				+ "/list.zul";
	}
	
	@RequestMapping(value = "/{path:.+$}/them")
	public String actionThem(@PathVariable String path) {
		return "forward:/WEB-INF/zul/home1.zul?resource=" + path + "&action=them&file=/WEB-INF/zul/hosocongtrinh/them.zul";
	}
	
	@RequestMapping(value = "/{path:.+$}/xem/{id:.+$}")
	public String actionXem(@PathVariable String path, @PathVariable Long id) {
		return "forward:/WEB-INF/zul/home1.zul?resource=" + path + "&action=xem&file=/WEB-INF/zul/hosocongtrinh/xem.zul&id=" + id;
	}
	
	@RequestMapping(value = "/{path:.+$}/sua/{id:.+$}")
	public String actionSua(@PathVariable String path, @PathVariable Long id) {
		return "forward:/WEB-INF/zul/home1.zul?resource=" + path + "&action=sua&file=/WEB-INF/zul/hosocongtrinh/sua.zul&id=" + id;
	}

	
	@RequestMapping(value = "/login")
	public String dangNhapBackend() {
		return "forward:/WEB-INF/zul/login.zul";
	}
	
	public final GioiTinhService getGioiTinhs() {
		return new GioiTinhService();
	}
	
	public final HoSoCongTrinhService getHoSoCongTrinhs() {
		return new HoSoCongTrinhService();
	}
	
	public final KeHoachNhuCauVonService getKeHoachNhuCauVons() {
		return new KeHoachNhuCauVonService();
	}
	
	public final Quyen getQuyen() {
		return getNhanVien().getTatCaQuyen();
	}

	public final VaiTroService getVaiTros() {
		return new VaiTroService();
	}
	
	public final ProcessEngine getProcess() {
		return processEngine;
	}
	
	public final ProcessService getProcessService() {
		return new ProcessService();
	}
	
	public final List<String> getNoiDungActive(){
		return Arrays.asList("chude", "baiviet", "video", "gallery", "lienket", "linhvuchoidap", "hoidaptructuyen", "faqcategory", "faq");
	}
	
	public boolean checkVaiTro(String vaiTro){
		if(vaiTro==null || vaiTro.isEmpty()){
			return false;
		}
		boolean rs = false;
		for (VaiTro vt : getNhanVien().getVaiTros()) {
			if(vaiTro.equals(vt.getAlias())){
				rs = true;
				break;
			}
		}
		return rs;// || getQuyen().get(vaiTro);
	}
		
}