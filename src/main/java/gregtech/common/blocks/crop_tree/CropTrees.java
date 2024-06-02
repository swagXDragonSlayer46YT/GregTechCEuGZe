package gregtech.common.blocks.crop_tree;

public class CropTrees {
    public static BananaTree BANANA_TREE;
    public static OrangeTree ORANGE_TREE;
    public static MangoTree MANGO_TREE;
    public static ApricotTree APRICOT_TREE;
    public static LemonTree LEMON_TREE;
    public static LimeTree LIME_TREE;
    public static OliveTree OLIVE_TREE;
    public static NutmegTree NUTMEG_TREE;
    public static CoconutTree COCONUT_TREE;

    public static void init() {
        BANANA_TREE = new BananaTree();
        ORANGE_TREE = new OrangeTree();
        MANGO_TREE = new MangoTree();
        APRICOT_TREE = new ApricotTree();
        LEMON_TREE = new LemonTree();
        LIME_TREE = new LimeTree();
        OLIVE_TREE = new OliveTree();
        NUTMEG_TREE = new NutmegTree();
        COCONUT_TREE = new CoconutTree();
    }
}
