import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class VlacekTest {
    Vlacek vlacek;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        vlacek = new Vlacek();
    }

    @Test
    void test1() {
        assertEquals(2, vlacek.getDelka());
        assertTrue(this::isPostovniVagonLast);
    }

    @Test
    void test2() {
        vlacek.pridatVagonek(VagonekType.DRUHA_TRIDA);
        vlacek.pridatVagonek(VagonekType.DRUHA_TRIDA);
        assertTrue(this::isPostovniVagonLast);
        Vagonek vagonek = vlacek.getVagonekByIndex(2);
        assertEquals(VagonekType.DRUHA_TRIDA, vagonek.getType());
        vlacek.pridatVagonek(VagonekType.PRVNI_TRIDA);
        vagonek = vlacek.getVagonekByIndex(2);
        assertEquals(VagonekType.PRVNI_TRIDA, vagonek.getType());
        vagonek = vlacek.getVagonekByIndex(3);
        assertEquals(VagonekType.DRUHA_TRIDA, vagonek.getType());
        assertEquals(3, vagonek.getUmisteni());
        assertTrue(this::isPostovniVagonLast);
    }

    @Test
    void test3() {
        test2();
        assertTrue(vlacek.getJidelniVozy().isEmpty());
        assertEquals(2, vlacek.getPocetVagonkuByType(VagonekType.DRUHA_TRIDA));
        assertEquals(1, vlacek.getPocetVagonkuByType(VagonekType.PRVNI_TRIDA));
    }

    @Test
    void test4() {
        test2(); // L,1,2,2,P
        vlacek.pridatVagonek(VagonekType.JIDELNI);
        Vagonek vagonek = vlacek.getVagonekByIndex(3);
        assertEquals(VagonekType.JIDELNI, vagonek.getType());
        vagonek = vlacek.getVagonekByIndex(5);
        assertEquals(VagonekType.DRUHA_TRIDA, vagonek.getType());
        assertTrue(this::isPostovniVagonLast);
    }

    @Test
    void test5() {
        test4(); // L,1,J,2,2,P
        List<Vagonek> jidelniVozy = vlacek.getJidelniVozy();
        assertEquals(1, jidelniVozy.size());
    }

    @Test
    void test6() {
        test4(); // L,1,J,2,2,P
        vlacek.pridatJidelniVagonek(); // L,1,J,2,J,2,P
        Vagonek vagonek = vlacek.getVagonekByIndex(5);

        assertEquals(VagonekType.JIDELNI, vagonek.getType());
        assertFalse(vlacek.getJidelniVozy().isEmpty());
        vagonek = vlacek.getLastVagonekByType(VagonekType.DRUHA_TRIDA);
        assertEquals(6, vagonek.getUmisteni());
    }

    @Test
    void test7() {
        test4(); // L,1,J,2,2,P
        vlacek.pridatVagonek(VagonekType.DRUHA_TRIDA);
        vlacek.pridatVagonek(VagonekType.PRVNI_TRIDA); // L,1,1,J,2,2,2,P
        vlacek.pridatVagonek(VagonekType.TRETI_TRIDA);
        vlacek.pridatVagonek(VagonekType.TRETI_TRIDA); // L,1,1,J,2,2,2,P,3,3
        Vagonek vagonek = vlacek.getVagonekByIndex(5);
        assertEquals(VagonekType.DRUHA_TRIDA, vagonek.getType());
        vagonek = vlacek.getVagonekByIndex(9);
        assertEquals(VagonekType.TRETI_TRIDA, vagonek.getType());
        assertFalse(this::isPostovniVagonLast);
    }

    @Test
    void test8() {
        test7(); // L,1,1,J,2,2,2,P,3,3
        assertFalse(this::isPostovniVagonLast);
        List<Vagonek> jidelniVozy = vlacek.getJidelniVozy();
        assertEquals(1, jidelniVozy.size());
        assertEquals(4, jidelniVozy.get(0).getUmisteni());
        Vagonek vagonek = vlacek.getVagonekByIndex(10);
        assertEquals(VagonekType.TRETI_TRIDA, vagonek.getType());
        vlacek.odebratPosledniVagonekByType(VagonekType.TRETI_TRIDA);
        vlacek.odebratPosledniVagonekByType(VagonekType.TRETI_TRIDA); // L,1,1,J,2,2,2,P
        assertTrue(this::isPostovniVagonLast);
    }

    @Test
    void test9() {
        test8();  // L,1,1,J,2,2,2,P
        vlacek.odebratPosledniVagonekByType(VagonekType.DRUHA_TRIDA);
        vlacek.odebratPosledniVagonekByType(VagonekType.DRUHA_TRIDA); // L,1,1,J,2,P
        assertEquals(1, vlacek.getPocetVagonkuByType(VagonekType.DRUHA_TRIDA));
        assertTrue(this::isPostovniVagonLast);
        vlacek.pridatVagonek(VagonekType.DRUHA_TRIDA);
        vlacek.pridatVagonek(VagonekType.PRVNI_TRIDA);
        vlacek.pridatVagonek(VagonekType.TRETI_TRIDA); // L,1,1,1,J,2,2,P,3
    }

    @Test
    void test10() {
        test9(); // L,1,1,1,J,2,2,P,3
        Vagonek vagonek = vlacek.getVagonekByIndex(4);
        assertEquals(VagonekType.PRVNI_TRIDA, vagonek.getType());
        vagonek = vlacek.getLastVagonekByType(VagonekType.POSTOVNI);
        assertEquals(8, vagonek.getUmisteni());
        vlacek.pridatVagonek(VagonekType.DRUHA_TRIDA);
        vlacek.pridatVagonek(VagonekType.DRUHA_TRIDA); // L,1,1,1,J,2,2,2,2,P,3
        vlacek.pridatVagonek(VagonekType.JIDELNI); // L,1,1,1,J,2,2,J,2,2,P,3
        assertFalse(this::isPostovniVagonLast);
    }

    @Test
    void test11() {
        test10(); // L,1,1,1,J,2,2,J,2,2,P,3
        vlacek.pridatVagonek(VagonekType.TRETI_TRIDA);
        vlacek.pridatVagonek(VagonekType.TRETI_TRIDA); // L,1,1,1,J,2,2,J,2,2,P,3,3,3
        assertEquals(3, vlacek.getPocetVagonkuByType(VagonekType.TRETI_TRIDA));
        Vagonek vagonek = vlacek.getLastVagonekByType(VagonekType.POSTOVNI);
        assertEquals(11, vagonek.getUmisteni());
    }

    @Test
    void test12() {
        test11(); // L,1,1,1,J,2,2,J,2,2,P,3,3,3
        assertEquals(14, vlacek.getDelka());
        vlacek.pridatVagonek(VagonekType.LUZKOVY);
        Vagonek vagonek = vlacek.getVagonekByIndex(6);
        assertEquals(VagonekType.LUZKOVY, vagonek.getType());
    }

    @Test
    void test13() {
        test11(); // L,1,1,1,J,2,2,J,2,2,P,3,3,3
        vlacek.odebratPosledniVagonekByType(VagonekType.JIDELNI);
        vlacek.odebratPosledniVagonekByType(VagonekType.JIDELNI);
        vlacek.odebratPosledniVagonekByType(VagonekType.PRVNI_TRIDA);
        vlacek.pridatVagonek(VagonekType.LUZKOVY);
        vlacek.pridatVagonek(VagonekType.LUZKOVY); // L,1,1,LU,LU,2,2,2,2,P,3,3,3
        Vagonek vagonek = vlacek.getVagonekByIndex(4);
        assertEquals(VagonekType.LUZKOVY, vagonek.getType());

    }

    private boolean isPostovniVagonLast() {
        Vagonek vagonek = vlacek.getVagonekByIndex(vlacek.getDelka());
        return VagonekType.POSTOVNI == vagonek.getType();
    }
}