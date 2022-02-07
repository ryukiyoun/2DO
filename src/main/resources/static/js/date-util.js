function toStringByFormatting(source, delimiter = '-') {
    const year = source.getFullYear();
    const month = ('0' + (source.getMonth() + 1)).slice(-2)
    const day = ('0' + source.getDate()).slice(-2)
    return [year, month, day].join(delimiter);
}
