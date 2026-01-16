package com.auroali.dfuhooks.v2.impl;

import com.auroali.dfuhooks.v2.api.SchemaBuilder;

import java.util.HashMap;

public class DFUHooks {
    public static final ThreadLocal<HashMap<Integer, SchemaBuilder>> BUILDERS = new ThreadLocal<>();
}
