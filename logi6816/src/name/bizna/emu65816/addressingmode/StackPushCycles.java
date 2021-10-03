package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.Width;

import java.util.function.Consumer;

import static name.bizna.emu65816.AddressingMode.Stack;

public class StackPushCycles
    extends InstructionCycles
{
  //22c
  public StackPushCycles(Consumer<Cpu65816> consumer, Width width)
  {
    super(Stack,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), E(consumer)),
          new BusCycle(Address(S()), Write_DataHigh(), SP_dec(), new NoteOne(width)),
          new BusCycle(Address(S()), Write_DataLow(), SP_dec(), DONE()));
  }
}

