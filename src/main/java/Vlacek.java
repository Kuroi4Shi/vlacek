import java.util.LinkedList;
import java.util.List;

public class Vlacek {

    private Vagonek lokomotiva = new Vagonek(VagonekType.LOKOMOTIVA);
    private Vagonek posledni = new Vagonek(VagonekType.POSTOVNI);
    private int delka = 2;

    public Vlacek(){
        lokomotiva.setNasledujici(posledni);
        lokomotiva.setUmisteni(1);
        posledni.setPredchozi(lokomotiva);
        posledni.setUmisteni(2);
    }

    /**
     * Přidávejte vagonky do vlaku
     * Podmínka je že vagonek první třídy musí být vždy řazen za předchozí vagonek toho typu, pokud žádný takový není je řazen rovnou za lokomotivu
     * vagonek 2 třídy musí být vždy řazen až za poslední vagonek třídy první
     * Poštovní vagonek musí být vždy poslední vagonek lokomotivy
     * Při vkládání vagonku nezapomeňte vagonku přiřadit danou pozici ve vlaku
     * !!!!!!! POZOR !!!!!! pokud přidáváte vagonek jinak než na konec vlaku musíte všem následujícím vagonkům zvýšit jejich umístění - doporučuji si pro tento účel vytvořit privátní metodu
     * @param type
     */
    public void pridatVagonek(VagonekType type) {
        Vagonek vagonek = new Vagonek(type);
        Vagonek lastPrvni = getLastVagonekByType(VagonekType.PRVNI_TRIDA);
        Vagonek lastJidelni = getLastVagonekByType(VagonekType.JIDELNI);
        switch (type) {
            case PRVNI_TRIDA:
                vagonek.setPredchozi(lokomotiva);
                lokomotiva.getNasledujici().setPredchozi(vagonek);
                vagonek.setNasledujici(lokomotiva.getNasledujici());
                lokomotiva.setNasledujici(vagonek);
                break;
            case DRUHA_TRIDA:
                if (getDelkaByType(VagonekType.PRVNI_TRIDA) == 0) {
                    vagonek.setPredchozi(lokomotiva);
                    lokomotiva.getNasledujici().setPredchozi(vagonek);
                    vagonek.setNasledujici(lokomotiva.getNasledujici());
                    lokomotiva.setNasledujici(vagonek);
                } else if (getDelkaByType(VagonekType.JIDELNI) == 1) {
                    vagonek.setPredchozi(lastPrvni.getNasledujici());
                    vagonek.setNasledujici(lastPrvni.getNasledujici());
                    lastPrvni.getNasledujici().setNasledujici(vagonek);
                } else if (getDelkaByType(VagonekType.JIDELNI) > 1) {
                    vagonek.setPredchozi(lastJidelni);
                    lastJidelni.getNasledujici().setPredchozi(vagonek);
                    vagonek.setNasledujici(lastJidelni.getNasledujici());
                    lastJidelni.setNasledujici(vagonek);
                } else {
                    vagonek.setPredchozi(lastPrvni);
                    vagonek.setNasledujici(lastPrvni.getNasledujici());
                    lastPrvni.setNasledujici(vagonek);
                }
                break;
            case JIDELNI:
                pridatJidelniVagonek();
                break;
        }
        serazeni();
        delka++;
    }


    private void serazeni() {
        Vagonek dalsiVagon = lokomotiva;
        for (int i = 1; i <= delka; i++) {
            dalsiVagon.setUmisteni(i);
            dalsiVagon = dalsiVagon.getNasledujici();
        }
    }

    public Vagonek getVagonekByIndex(int index) {
        int i = 1;
        Vagonek atIndex = lokomotiva;
        while(i < index) {
            atIndex = atIndex.getNasledujici();
            i++;
        }
        return atIndex;
    }


    /**
     * Touto metodou si můžete vrátit poslední vagonek daného typu
     * Pokud tedy budu chtít vrátit vagonek typu lokomotiva dostanu hned první vagonek
     * @param type
     * @return
     */
    public Vagonek getLastVagonekByType(VagonekType type) {
        Vagonek newVagonek = new Vagonek(type);
        switch (type) {
            case PRVNI_TRIDA:
                for (int i = 1; i < getDelkaByType(type); i++) {
                    newVagonek = getVagonekByIndex(i);
                }
                break;
            case DRUHA_TRIDA:
                for (int i = 1+getDelkaByType(VagonekType.PRVNI_TRIDA); i < getDelkaByType(type); i++) {
                    newVagonek = getVagonekByIndex(i);
                }
                break;
            case JIDELNI:
                for (int i = 0; i < 2+getDelkaByType(VagonekType.JIDELNI)+Math.round((getDelkaByType(VagonekType.DRUHA_TRIDA)/2)); i++) {
                    newVagonek = getVagonekByIndex(i);
                }
                break;
        }
        return newVagonek;
    }

    /**
     * Tato funkce přidá jídelní vagonek za poslední vagonek první třídy, pokud jídelní vagonek za vagonkem první třídy již existuje
     * tak se další vagonek přidá nejblíže středu vagonků druhé třídy
     * tzn: pokud budu mít č osobních vagonků tak zařadím jídelní vagonek za 2 osobní vagónek
     * pokud budu mít osobních vagonků 5 zařadím jídelní vagonek za 3 osobní vagonek
     */
    public void pridatJidelniVagonek() {
        Vagonek jidelni = new Vagonek(VagonekType.JIDELNI);
        Vagonek lastPrvni = getLastVagonekByType(VagonekType.PRVNI_TRIDA);
        if (getDelkaByType(VagonekType.JIDELNI) == 0) {
            if (getDelkaByType(VagonekType.PRVNI_TRIDA) > 0) {
                lastPrvni.getNasledujici().setPredchozi(jidelni);
                jidelni.setPredchozi(lastPrvni);
                jidelni.setNasledujici(lastPrvni.getNasledujici());
                lastPrvni.setNasledujici(jidelni);
            } else {
                lokomotiva.getNasledujici().setPredchozi(jidelni);
                jidelni.setPredchozi(lokomotiva);
                jidelni.setNasledujici(lokomotiva.getNasledujici());
                lokomotiva.setNasledujici(jidelni);
            }
        } else {
            Vagonek atIndex = getVagonekByIndex(Math.round((getDelkaByType(VagonekType.DRUHA_TRIDA)/2)+getDelkaByType(VagonekType.PRVNI_TRIDA)+2));
            atIndex.getNasledujici().setPredchozi(jidelni);
            jidelni.setPredchozi(atIndex);
            atIndex.setNasledujici(jidelni);
        }
    }

    /**
     * Funkce vrátí počet vagonků daného typu
     * Dobré využití se najde v metodě @method(addJidelniVagonek)
     * @param type
     * @return
     */
    public int getDelkaByType(VagonekType type) {
        int delkaTypu = 0;
        for (int i = 1; i <= delka; i++) {
            if (getVagonekByIndex(i).getType() == type) {
                delkaTypu++;
            }
        }
        return delkaTypu;
    }

    /**
     * Hledejte jidelni vagonky
     * @return
     */
    public List<Vagonek> getJidelniVozy() {
        List<Vagonek> jidelniVozy = new LinkedList<>();

        return jidelniVozy;
    }

    /**
     * Odebere poslední vagonek daného typu
     * !!!! POZOR !!!!! pokud odebíráme z prostředku vlaku musíme zbývající vagonky projít a snížit jejich umístění ve vlaku
     * @param type
     */
    public void odebratPosledniVagonekByType(VagonekType type) {

    }

    public int getDelka() {
        return delka;
    }
}
