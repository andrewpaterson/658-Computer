package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.Accumulator;

public class AccumulatorCycles
    extends InstructionCycles
{
  //8
  public AccumulatorCycles()
  {
    super(Accumulator,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), new Execute1(), new Execute2(), DONE()));
  }
}

