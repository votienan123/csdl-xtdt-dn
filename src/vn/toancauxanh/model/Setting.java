package vn.toancauxanh.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.zkoss.util.resource.Labels;


@Entity
@Table(name = "settings")
public class Setting extends Model<Setting> {
	private int widthMedium;
	private int widthSmall;
	private long dem;
	
	public final static String IMG_MEDIUM_WIDTH = Labels.getLabel("conf.image.medium.width", "460");
	public final static String IMG_SMALL_WIDTH = Labels.getLabel("conf.image.small.width", "220");
	
	public int getWidthMedium() {
		return widthMedium;
	}

	public void setWidthMedium(final int _widthMedium) {
		if (_widthMedium == 0) {
			this.widthMedium = Integer.parseInt(IMG_MEDIUM_WIDTH);
		} else {
			this.widthMedium = _widthMedium;
		}
	}

	public int getWidthSmall() {
		return widthSmall;
	}

	public void setWidthSmall(final int _widthSmall) {
		if (_widthSmall == 0) {
			this.widthSmall = Integer.parseInt(IMG_SMALL_WIDTH);
		} else {
			this.widthSmall = _widthSmall;
		}
	}

	public long getDem() {
		return dem;
	}

	public void setDem(long dem1) {
		this.dem = dem1;
	}	

	public void addCounter() {
		dem++;
		transactioner().execute(new TransactionCallbackWithoutResult() {
			@Override
			protected void doInTransactionWithoutResult(TransactionStatus arg0) {
				em().merge(Setting.this);
			}
		});
	}
}
