package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.Width;

import java.util.function.Consumer;

import static name.bizna.cpu.AddressingMode.Immediate;

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

