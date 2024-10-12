package com.auroali.dfuhooks.builtinfixes;

import com.mojang.datafixers.DataFix;
import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.TypeRewriteRule;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import net.minecraft.datafixer.TypeReferences;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Allows changing item ids between versions.
 */
public class DFUHooksItemIdFix extends DataFix {
    String modid;
    Map<String, String> idMap;

    public DFUHooksItemIdFix(Schema outputSchema, boolean changesType, String modid, Map<String, String> idMap) {
        super(outputSchema, changesType);
        this.modid = modid;
        this.idMap = idMap;
    }

    public Optional<Dynamic<?>> fixId(Dynamic<?> dynamic) {
        return dynamic.get("id").asString().result().map(id -> {
            if(idMap.containsKey(id))
                return dynamic.set("id", dynamic.createString(idMap.get(id)));
            return null;
        });
    }

    @Override
    protected TypeRewriteRule makeRule() {
        return this.writeFixAndRead(
                "DFU Hooks Item Id Fix - %s".formatted(modid),
                getInputSchema().getType(TypeReferences.ITEM_STACK),
                getOutputSchema().getType(TypeReferences.ITEM_STACK),
                dynamic -> {
                    Optional<Dynamic<?>> newDynamic = fixId(dynamic);
                    return DataFixUtils.orElse(newDynamic, dynamic);
                }
        );
    }
}
