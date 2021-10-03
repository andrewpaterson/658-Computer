package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.Width;

import java.util.function.Consumer;

import static name.bizna.emu65816.AddressingMode.Immediate;

public class ImmediateCycles
    extends InstructionCycles
{
  //18
  public ImmediateCycles(Consumer<Cpu65816> operation, Width width)
  {
    super(Immediate,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), PC_inc(), Read_DataLow(), E8Bit(operation), DONE8Bit(width)),
          new BusCycle(Address(PBR(), PC()), PC_inc(), Read_DataHigh(), E16Bit(operation), DONE16Bit(width)));
  }
}

