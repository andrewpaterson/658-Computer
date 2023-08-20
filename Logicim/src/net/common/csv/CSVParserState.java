package net.common.csv;

public enum CSVParserState
{
  initial_entry_state,
  one_quote_state,
  two_quotes_state,
  unquoted_value_state,
  end_of_line_state,
  end_of_file_state
}

