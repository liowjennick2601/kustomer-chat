jest.mock('@kustomer/chat-react-native', () => {
  const ActualFlashList = jest.requireActual(
    '@kustomer/chat-react-native'
  ).FlashList;
  class MockFlashList extends ActualFlashList {
    componentDidMount() {
      super.componentDidMount();
      this.rlvRef?._scrollComponent?._scrollViewRef?.props.onLayout({
        nativeEvent: { layout: { height: 900, width: 400 } },
      });
    }
  }
  return {
    ...jest.requireActual('@kustomer/chat-react-native'),
    FlashList: MockFlashList,
  };
});
