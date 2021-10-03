package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.Width;

import java.util.function.Consumer;

import static name.bizna.emu65816.AddressingMode.AbsoluteLong;

public class AbsoluteLongWriteCycles
    extends InstructionCycles
{
  //4a
  public AbsoluteLongWriteCycles(Consumer<Cpu65816> operation, Width width)
  {
    super(AbsoluteLong,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAL(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAH(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAB(), PC_inc()),
          new BusCycle(Address(AAB(), AA()), E(operation), Write_DataLow(), DONE8Bit(width)),
          new BusCycle(Address(AAB(), AA(), o(1)), Write_DataHigh(), DONE16Bit(width)));
  }
}

