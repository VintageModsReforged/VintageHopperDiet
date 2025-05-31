package dev.crossvas.vintagehopperdiet.asm;

import cpw.mods.fml.relauncher.IClassTransformer;
import dev.crossvas.vintagehopperdiet.VintageHopperDiet;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class BlockHopperMixin implements IClassTransformer {

    private final HashMap<String, String> obfStrings;

    public BlockHopperMixin() {
        this.obfStrings = new HashMap<String, String>();
        this.obfStrings.put("className", "amw");
        this.obfStrings.put("myClass", "dev/crossvas/vintagehopperdiet/asm/HopperHelper");
        // addCollisionBoxesToList
        this.obfStrings.put("myAddCollisionBoxesToList", "addCollisionBoxesToList");
        this.obfStrings.put("addCollisionBoxesToList", "a");
        this.obfStrings.put("addCollisionBoxesToListParams", "(Laab;IIILaqx;Ljava/util/List;Lmp;)V");
        // getSelectedBoundingBoxFromPool
        this.obfStrings.put("myGetSelectedBoundingBoxFromPool", "getSelectedBoundingBoxFromPool");
        this.obfStrings.put("getSelectedBoundingBoxFromPool", "c_");
        this.obfStrings.put("getSelectedBoundingBoxFromPoolParams", "(Laab;III)Laqx;");
        // collisionRayTrace
        this.obfStrings.put("myCollisionRayTrace", "collisionRayTrace");
        this.obfStrings.put("collisionRayTrace", "a");
        this.obfStrings.put("collisionRayTraceParams", "(Laab;IIILarc;Larc;)Lara;");
    }

    @Override
    public byte[] transform(String obfName, String transformedName, byte[] basicClass) {
        return obfName.equals(obfStrings.get("className")) ? transform(basicClass) : basicClass;
    }

    private byte[] transform(byte[] basicClass) {
        VintageHopperDiet.LOGGER.info("[Vintage Hopper Diet - ASM] Transforming Classes!");
        VintageHopperDiet.LOGGER.info("[Vintage Hopper Diet - ASM] Class Transformation running on " + this.obfStrings.get("className") + "...");
        ClassNode classNode = new ClassNode();
        ClassReader classReader = new ClassReader(basicClass);
        classReader.accept(classNode, 0);
        List<MethodNode> methods = classNode.methods;
        Iterator<MethodNode> methodsIterator = methods.iterator();
        // remove existing - addCollisionBoxesToList method
        while (methodsIterator.hasNext()) {
            MethodNode method = methodsIterator.next();
            if (method.name.equals(obfStrings.get("addCollisionBoxesToList")) && method.desc.equals(obfStrings.get("addCollisionBoxesToListParams"))) {
                VintageHopperDiet.LOGGER.info("[Vintage Hopper Diet - ASM] Found target method " + method.name + method.desc + "! Searching for landmarks...");
                VintageHopperDiet.LOGGER.info("[Vintage Hopper Diet - ASM] Patching method " + obfStrings.get("className") + "/" + method.name + method.desc + "...");
                methodsIterator.remove();
                break;
            }
        }

        String hopperHelper = obfStrings.get("myClass");
        // add our addCollisionBoxesToList
        MethodNode addBoxes = new MethodNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL,
                obfStrings.get("addCollisionBoxesToList"),
                obfStrings.get("addCollisionBoxesToListParams"),
                null, null);
        InsnList cbCode = addBoxes.instructions;
        cbCode.add(new VarInsnNode(Opcodes.ALOAD, 1)); // world
        cbCode.add(new VarInsnNode(Opcodes.ILOAD, 2)); // x
        cbCode.add(new VarInsnNode(Opcodes.ILOAD, 3)); // y
        cbCode.add(new VarInsnNode(Opcodes.ILOAD, 4)); // z
        cbCode.add(new VarInsnNode(Opcodes.ALOAD, 5)); // mask
        cbCode.add(new VarInsnNode(Opcodes.ALOAD, 6)); // list
        cbCode.add(new VarInsnNode(Opcodes.ALOAD, 7)); // entity
        cbCode.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                hopperHelper,
                obfStrings.get("myAddCollisionBoxesToList"),
                obfStrings.get("addCollisionBoxesToListParams")));
        cbCode.add(new InsnNode(Opcodes.RETURN));
        methods.add(addBoxes);
        VintageHopperDiet.LOGGER.info("[Vintage Hopper Diet - ASM] Patching method " + obfStrings.get("className") + "/" + addBoxes.name + addBoxes.desc + "...");

        // add getSelectedBoundingBoxFromPool
        MethodNode getBB = new MethodNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL,
                obfStrings.get("getSelectedBoundingBoxFromPool"),
                obfStrings.get("getSelectedBoundingBoxFromPoolParams"),
                null, null);

        InsnList bbCode = getBB.instructions;
        bbCode.add(new VarInsnNode(Opcodes.ALOAD, 1)); // world
        bbCode.add(new VarInsnNode(Opcodes.ILOAD, 2)); // x
        bbCode.add(new VarInsnNode(Opcodes.ILOAD, 3)); // y
        bbCode.add(new VarInsnNode(Opcodes.ILOAD, 4)); // z
        bbCode.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                hopperHelper,
                obfStrings.get("myGetSelectedBoundingBoxFromPool"),
                obfStrings.get("getSelectedBoundingBoxFromPoolParams")));
        bbCode.add(new InsnNode(Opcodes.ARETURN));
        methods.add(getBB);
        VintageHopperDiet.LOGGER.info("[Vintage Hopper Diet - ASM] Patching method " + obfStrings.get("className") + "/" + getBB.name + getBB.desc + "...");

        // add collisionRayTrace
        MethodNode rayTrace = new MethodNode(Opcodes.ACC_PUBLIC | Opcodes.ACC_FINAL,
                obfStrings.get("collisionRayTrace"),
                obfStrings.get("collisionRayTraceParams"),
                null, null);

        InsnList rtCode = rayTrace.instructions;
        rtCode.add(new VarInsnNode(Opcodes.ALOAD, 1)); // world
        rtCode.add(new VarInsnNode(Opcodes.ILOAD, 2)); // x
        rtCode.add(new VarInsnNode(Opcodes.ILOAD, 3)); // y
        rtCode.add(new VarInsnNode(Opcodes.ILOAD, 4)); // z
        rtCode.add(new VarInsnNode(Opcodes.ALOAD, 5)); // start
        rtCode.add(new VarInsnNode(Opcodes.ALOAD, 6)); // end
        rtCode.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
                hopperHelper,
                obfStrings.get("myCollisionRayTrace"),
                obfStrings.get("collisionRayTraceParams")));
        rtCode.add(new InsnNode(Opcodes.ARETURN));
        methods.add(rayTrace);
        VintageHopperDiet.LOGGER.info("[Vintage Hopper Diet - ASM] Patching method " + obfStrings.get("className") + "/" + rayTrace.name + rayTrace.desc + "...");

        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        classNode.accept(writer);
        return writer.toByteArray();
    }
}
