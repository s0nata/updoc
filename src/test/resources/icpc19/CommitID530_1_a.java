/**
 * Convert file to HTML.
 *
 * Uploads an input file. The response includes an HTML version of the document.
 *
 * @param convertToHtmlOptions the {@link ConvertToHtmlOptions} containing the options for the call
 * @return a {@link ServiceCall} with a response type of {@link HTMLReturn}
 */
public ServiceCall<HTMLReturn> convertToHtml(ConvertToHtmlOptions convertToHtmlOptions) {
        Validator.notNull(convertToHtmlOptions, "convertToHtmlOptions cannot be null");
        String[] pathSegments = { "v1/html_conversion" };
        RequestBuilder builder = RequestBuilder.post(RequestBuilder.constructHttpUrl(getEndPoint(), pathSegments));
        builder.query(VERSION, versionDate);
        if (convertToHtmlOptions.modelId() != null) {
        builder.query("model_id", convertToHtmlOptions.modelId());
        }
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder();
        multipartBuilder.setType(MultipartBody.FORM);
        RequestBody fileBody = RequestUtils.inputStreamBody(convertToHtmlOptions.file(), convertToHtmlOptions
        .fileContentType());
        multipartBuilder.addFormDataPart("file", convertToHtmlOptions.filename(), fileBody);
        builder.body(multipartBuilder.build());
        return createServiceCall(builder.build(), ResponseConverterUtils.getObject(HTMLReturn.class));
        }