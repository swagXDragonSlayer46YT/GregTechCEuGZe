package gregtech.common.metatileentities.multi.electric;

import gregtech.api.metatileentity.GCYMRecipeMapMultiblockController;

import gregtech.api.metatileentity.multiblock.RecipeMapMultiblockController;
import gregtech.client.renderer.texture.Textures;
import gregtech.common.blocks.BlockLargeMultiblockCasing;

import gregtech.common.blocks.BlockMetalCasing;

import gregtech.common.blocks.BlockMetalCasing2;
import gregtech.api.unification.material.Materials;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

import org.jetbrains.annotations.NotNull;

import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.interfaces.IGregTechTileEntity;
import gregtech.api.metatileentity.multiblock.IMultiblockPart;
import gregtech.api.pattern.BlockPattern;
import gregtech.api.pattern.FactoryBlockPattern;
import gregtech.api.recipes.RecipeMaps;
import gregtech.client.renderer.ICubeRenderer;
import gregtech.client.renderer.texture.cube.OrientedOverlayRenderer;
import gregtech.common.blocks.BlockBoilerCasing;
import gregtech.common.blocks.MetaBlocks;

public class MetaTileEntityBallMill extends RecipeMapMultiblockController {

    public MetaTileEntityBallMill(ResourceLocation metaTileEntityId) {
        super(metaTileEntityId, RecipeMaps.MILLING_RECIPES);
    }

    @Override
    public MetaTileEntity createMetaTileEntity(IGregTechTileEntity metaTileEntityHolder) {
        return new MetaTileEntityBallMill(this.metaTileEntityId);
    }

    @Override
    protected @NotNull BlockPattern createStructurePattern() {
        return FactoryBlockPattern.start()
                //start back
                .aisle("*#**#*","*#**#*","*####*","*#II#*","*#II#*","*####*","******")
                .aisle("*#**#*","******","*XXXX*","*XXXX*","*XXXX*","*XXXX*","******")
                .aisle("*#**#*","*XXXX*","XXXXXX","XX**XX","XX**XX","XXXXXX","*XXXX*")
                //end back
                //start mid
                .aisle("*#**#*","******","**XX**","*X  X*","*X  X*","**XX**","******")
                .aisle("*#**#*","*HHHH*","HHXXHH","HX  XH","HX  XH","**XX**","******")
                .aisle("*#**#*","******","**XX**","*X  X*","*X  X*","**XX**","******")
                .aisle("*#**#*","*HHHH*","HHXXHH","HX  XH","HX  XH","**XX**","******")
                .aisle("*#**#*","******","**XX**","*X  X*","*X  X*","**XX**","******")
                .aisle("*#**#*","*HHHH*","HHXXHH","HX  XH","HX  XH","**XX**","******")
                .aisle("*#**#*","******","**XX**","*X  X*","*X  X*","**XX**","******")
                .aisle("*#**#*","*HHHH*","HHXXHH","HX  XH","HX  XH","**XX**","******")
                //end mid
                //start front
                .aisle("*#**#*","******","**XX**","*X  X*","*X  X*","**XX**","******")
                .aisle("*#**#*","*XXXX*","XXXXXX","XX**XX","XX**XX","XXXXXX","*XXXX*")
                .aisle("*#**#*","******","*XXXX*","*XXXX*","*XXXX*","*XXXX*","******")
                .aisle("*#**#*","*#**#*","*####*","*#II#*","*#SI#*","*####*","******")
                //end front
                .where('*', any())
                .where(' ', air())
                .where('#', states(MetaBlocks.METAL_CASING_2.getState(BlockMetalCasing2.MetalCasingType2.STRUCTURAL)))
                .where('S', selfPredicate())
                .where('I', autoAbilities().or(states(MetaBlocks.FRAMES.get(Materials.StainlessSteel).getBlock(Materials.StainlessSteel))))
                .where('X', states(MetaBlocks.METAL_CASING_2.getState(BlockMetalCasing2.MetalCasingType2.CERAMIC_MILL)))
                .where('H', states(MetaBlocks.FRAMES.get(Materials.StainlessSteel).getBlock(Materials.StainlessSteel)))
                .build();
    }

    @Override
    public ICubeRenderer getBaseTexture(IMultiblockPart iMultiblockPart) {
        return Textures.CERAMIC_MILL;
    }

    @Override
    protected @NotNull OrientedOverlayRenderer getFrontOverlay() {
        return Textures.INDUSTRIAL_REFRIGERATOR_OVERLAY;
    }
}
