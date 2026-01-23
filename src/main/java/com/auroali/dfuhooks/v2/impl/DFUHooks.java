package com.auroali.dfuhooks.v2.impl;

import com.auroali.dfuhooks.v2.api.SchemaBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;

@ApiStatus.Internal
public class DFUHooks {
    public static final ThreadLocal<HashMap<Integer, SchemaBuilder>> BUILDERS = new ThreadLocal<>();
    public static final Logger LOGGER = LoggerFactory.getLogger("dfuhooks");
}
