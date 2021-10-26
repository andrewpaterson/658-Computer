package net.common;

public interface Pins<SNAPSHOT extends Snapshot, PINS extends Pins<SNAPSHOT, PINS, ? extends IntegratedCircuit<SNAPSHOT, PINS>>, INTEGRATED_CIRCUIT extends IntegratedCircuit<SNAPSHOT, PINS>>
{
  INTEGRATED_CIRCUIT getIntegratedCircuit();

  void setIntegratedCircuit(INTEGRATED_CIRCUIT integratedCircuit);
}

