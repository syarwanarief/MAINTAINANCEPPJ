package medio.maintainanceppj.getterSetter;

public class getter_tambah {

    String kegiatan;
    String tgl;
    String jam;

    public getter_tambah(String kegiatan) {
        this.kegiatan = kegiatan;
    }

    public getter_tambah(String kegiatan, String tgl) {
        this.kegiatan = kegiatan;
        this.tgl = tgl;
    }

    public getter_tambah(String kegiatan, String tgl, String jam) {
        this.kegiatan = kegiatan;
        this.tgl = tgl;
        this.jam = jam;
    }
}
