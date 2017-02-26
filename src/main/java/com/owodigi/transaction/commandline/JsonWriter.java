package com.owodigi.transaction.commandline;

import java.io.PrintStream;

public class JsonWriter {
    private final PrintStream out;

    public JsonWriter(final PrintStream out) {
        this.out = out;
    }

    public JsonWriter writeStartArray() {
        out.print("[");
        return this;
    }

    public JsonWriter writeEndArray() {
        out.print("]");
        return this;
    }

    public JsonWriter writeStartObject() {
        out.print("{");
        return this;
    }

    public JsonWriter writeEndObject(final boolean last) {
        out.print("}");
        if (!last) {
            out.print(",");
        }
        return this;
    }
    
    public JsonWriter writeEndObject() {
        out.print("}");
        return this;
    }

    public JsonWriter writeStringField(final String field, final Object value, final boolean last) {
        writeString(field).write(":").writeString(value);
        if (last == false) {
            write(",");
        }
        return this;
    }

    public JsonWriter writeStringField(final Object field) {
        writeString(field).write(":");
        return this;
    }
    
    public JsonWriter writeStringField(final String field, final Object value) {
        writeStringField(field, value, false);
        return this;
    }

    public JsonWriter writeString(final Object obj) {
        out.append("\"").append(obj.toString()).append("\"");
        return this;
    }
    
    public JsonWriter write(final Object text) {
        out.print(text);
        return this;
    }
}
