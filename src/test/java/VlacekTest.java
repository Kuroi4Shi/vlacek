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
        assertEquals(2, vlacek.getDelkaByType(VagonekType.DRUHA_TRIDA));
        assertEquals(1, vlacek.getDelkaByType(VagonekType.PRVNI_TRIDA));
    }

    @Test
    void test4() {
        test2(); // L,1,J,2,2,P
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
        vlacek.pridatJidelniVagonek();
        Vagonek vagonek = vlacek.getVagonekByIndex(5);
        assertEquals(VagonekType.JIDELNI, vagonek.getType());
        assertFalse(vlacek.getJidelniVozy().isEmpty());
    }

    @Test
    void test7() {
        test4(); // L,1,J,2,2,P
        vlacek.pridatVagonek(VagonekType.DRUHA_TRIDA);
        vlacek.pridatVagonek(VagonekType.DRUHA_TRIDA);
        vlacek.pridatVagonek(VagonekType.DRUHA_TRIDA);
        vlacek.pridatVagonek(VagonekType.PRVNI_TRIDA); // L,1,1,J,2,2,2,2,2,P
        assertEquals(2, vlacek.getDelkaByType(VagonekType.PRVNI_TRIDA));
        assertEquals(1, vlacek.getDelkaByType(VagonekType.LOKOMOTIVA));
        vlacek.pridatJidelniVagonek();
        assertTrue(this::isPostovniVagonLast);
        List<Vagonek> jidelniVozy = vlacek.getJidelniVozy();
        assertEquals(4, jidelniVozy.get(0).getUmisteni());
        assertEquals(8, jidelniVozy.get(1).getUmisteni());
    }

    @Test
    void test8() {
        test7(); // L,1,1,J,2,2,2,J,2,2,P
        vlacek.odebratPosledniVagonekByType(VagonekType.JIDELNI);
        assertTrue(this::isPostovniVagonLast);
        List<Vagonek> jidelniVozy = vlacek.getJidelniVozy();
        assertEquals(1, jidelniVozy.size());
        assertEquals(4, jidelniVozy.get(0).getUmisteni());
    }

    @Test
    void test9() {
        test8(); // L,1,1,J,2,2,2,2,2,P
        vlacek.odebratPosledniVagonekByType(VagonekType.DRUHA_TRIDA);
        vlacek.odebratPosledniVagonekByType(VagonekType.DRUHA_TRIDA);
        assertEquals(3, vlacek.getDelkaByType(VagonekType.DRUHA_TRIDA));
        assertTrue(this::isPostovniVagonLast);
    }

    @Test
    void test10() {
        test9(); // L,1,1,J,2,2,2,P
        Vagonek vagonek = vlacek.getVagonekByIndex(4);
        assertEquals(VagonekType.JIDELNI, vagonek.getType());
        vagonek = vlacek.getLastVagonekByType(VagonekType.POSTOVNI);
        assertEquals(8, vagonek.getUmisteni());
    }

    private boolean isPostovniVagonLast() {
        Vagonek vagonek = vlacek.getVagonekByIndex(vlacek.getDelka());
        return VagonekType.POSTOVNI == vagonek.getType();
    }
}