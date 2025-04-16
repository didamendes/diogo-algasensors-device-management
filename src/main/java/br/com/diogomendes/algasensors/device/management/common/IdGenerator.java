package br.com.diogomendes.algasensors.device.management.common;

import io.hypersistence.tsid.TSID;

import java.util.Optional;

public class IdGenerator {

    private static final TSID.Factory tsidFaFACTORY;

    static {
        Optional.ofNullable(System.getenv("tsid.node"))
                .ifPresent(tsidNode -> System.setProperty("tsid.node", tsidNode));

        Optional.ofNullable(System.getenv("tsid.node.count"))
                .ifPresent(tsidNodeCount -> System.setProperty("tsid.node.count", tsidNodeCount));

        tsidFaFACTORY = TSID.Factory.builder().build();
    }

    private IdGenerator() { }

    public static TSID generateTSID() {
        return tsidFaFACTORY.generate();
    }

}
