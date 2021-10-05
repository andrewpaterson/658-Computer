package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;

import java.util.function.Consumer;

import static name.bizna.emu65816.AddressingMode.Accumulator;

public class AccumulatorCycles
    extends InstructionCycles
{
  //8
  public AccumulatorCycles(Consumer<Cpu65816> operation)
  {
    super(Accumulator,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), E(operation), DONE()));
  }
}

