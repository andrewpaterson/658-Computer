package net.assembler.sixteenhigh.tokeniser;

public enum SixteenHighKeywordCode
{
  int8,
  uint8,
  int16,
  uint16,
  int24,
  uint24,
  int32,
  uint32,
  int64,
  uint64,
  float8,
  float16,
  float32,
  float64,
  float128,
  bool,
  assign,             // =
  add,                // +
  subtract,           // -
  multiply,           // *
  divide,             // /
  modulus,            // %
  shift_left,         // <<
  shift_right,        // >>
  ushift_right,       // >>>
  and,                // &
  or,                 // |
  xor,                // ^
  not,                // ~
  if_greater,         // if>
  if_equals,          // if=
  if_less,            // if<
  if_greater_equals,  // if>=
  if_less_equals,     // if<=
  if_not_equals,      // if!=
  add_assign,         // +=
  subtract_assign,    // -=
  multiply_assign,    // *=
  divide_assign,      // /=
  modulus_assign,     // %=
  shift_left_assign,  // <<=
  shift_right_assign, // >>=
  ushift_right_assign,// >>>=
  and_assign,         // &=
  or_assign,          // |=
  xor_assign,         // ^=
  not_assign,         // ~=
  increment,          // ++
  decrement,          // --
  go,
  gosub,              // gosub
  subtract_compare,   // ?-
  and_compare,        // ?&
  is_true,            // ?
  is_false,           // ?!
  test_set,           // ts
  test_reset,         // tr
  pull,               // <
  push,               // >
  ret,                // return
  end,
  struct,
  start_address,
  end_address,
  access_mode,
  read_only,
  write_only,
  read_write,
  access_time,
  open_round,         // (
  close_round,        // )
  open_square,        // [
  close_square,       // ]

  UNKNOWN
}
