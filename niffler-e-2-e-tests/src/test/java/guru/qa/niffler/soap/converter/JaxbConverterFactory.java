package guru.qa.niffler.soap.converter;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.annotation.XmlRootElement;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * A {@linkplain Converter.Factory converter} which uses JAXB for XML. All validation events are
 * ignored.
 */
public final class JaxbConverterFactory extends Converter.Factory {
    static final MediaType XML = MediaType.get("application/xml; charset=utf-8");

    /**
     * Create an instance using a default {@link JAXBContext} instance for conversion.
     */
    public static JaxbConverterFactory create() {
        return new JaxbConverterFactory(null);
    }

    /**
     * Create an instance using {@code context} for conversion.
     */
    @SuppressWarnings("ConstantConditions") // Guarding public API nullability.
    public static JaxbConverterFactory create(JAXBContext context) {
        if (context == null) throw new NullPointerException("context == null");
        return new JaxbConverterFactory(context);
    }

    /**
     * If null, a new JAXB context will be created for each type to be converted.
     */
    private final @Nullable JAXBContext context;

    private JaxbConverterFactory(@Nullable JAXBContext context) {
        this.context = context;
    }

    @Override
    public @Nullable Converter<?, RequestBody> requestBodyConverter(
            Type type,
            Annotation[] parameterAnnotations,
            Annotation[] methodAnnotations,
            Retrofit retrofit) {
        if (type instanceof Class && ((Class<?>) type).isAnnotationPresent(XmlRootElement.class)) {
            return new JaxbRequestConverter<>(contextForType((Class<?>) type), (Class<?>) type);
        }
        return null;
    }

    @Override
    public @Nullable Converter<ResponseBody, ?> responseBodyConverter(
            Type type, Annotation[] annotations, Retrofit retrofit) {
        if (type instanceof Class && ((Class<?>) type).isAnnotationPresent(XmlRootElement.class)) {
            return new JaxbResponseConverter<>(contextForType((Class<?>) type), (Class<?>) type);
        }
        return null;
    }

    private JAXBContext contextForType(Class<?> type) {
        try {
            return context != null ? context : JAXBContext.newInstance(type);
        } catch (JAXBException e) {
            throw new IllegalArgumentException(e);
        }
    }
}