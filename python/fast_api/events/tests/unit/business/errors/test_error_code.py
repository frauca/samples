from fairs_bg.business.errors.error_code import ErrorCode


def test_no_code_duplications() -> None:
    all_codes = [e.code for e in ErrorCode]

    assert len(all_codes) == len(set(all_codes))

def test_all_enum_has_code_and_title() -> None:
    for e in ErrorCode:
        assert e.code 
        assert e.title

def test_all_codes_format() -> None:
    for e in ErrorCode:
        assert len(e.code ) == 6
        assert int(e.code)