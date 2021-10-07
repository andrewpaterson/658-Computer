package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.Width;

import java.util.function.Consumer;

import static name.bizna.cpu.AddressingMode.Absolute;

public class AbsoluteCycles
    extends InstructionCycles
{
  //1a
  public AbsoluteCycles(Consumer<Cpu65816> operation, Width width)
  {
    super(Absolute,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAL(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAH(), PC_inc()),
          new BusCycle(Address(DBR(), AA()), Read_DataLow(), E8Bit(operation), DONE8Bit(width)),
          new BusCycle(Address(DBR(), AA(), o(1)), Read_DataHigh(), E16Bit(operation), DONE16Bit(width)));
  }
}

